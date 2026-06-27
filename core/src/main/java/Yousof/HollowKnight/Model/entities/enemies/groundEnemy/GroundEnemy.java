package Yousof.HollowKnight.Model.entities.enemies.groundEnemy;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Enum.state.GroundEnemyState;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;

public class GroundEnemy extends Enemy {
    private int health;
    private int damage = 5;

    private TextureRegion currentFrame;
    private Animations animation;
    private float yOffset;
    private float stateTime = 0;

    private GroundEnemyState previousState;
    private GroundEnemyState currentState;

    private final float speed;
    private final float halfWidth;
    private final float halfHeight;
    private boolean facingRight = true;
    private boolean physicsCleanedUp = false;

    private GroundSurroundSensors sensors;


    public GroundEnemy(World world, float x, float y, float width, float height, float speed , int health , Animations anim , float yOffset) {
        this.speed = speed;
        this.halfWidth = width / 2f;
        this.halfHeight = height / 2f;
        this.health = health;
        this.animation = anim;
        this.yOffset = yOffset;
        this.currentState = GroundEnemyState.RUN;
        sensors = new GroundSurroundSensors();
        createBody(world, new Vector2(x, y));
    }

    @Override
    public void update(float dt) {
        handleDeath();
        handleSensors(dt);
        handleAnimation(dt);
        handleVelacity(dt);
    }

    @Override
    public void draw(Batch batch) {
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + yOffset;
        batch.draw(currentFrame, drawX, drawY);
    }

    public void handleDeath(){
        if(currentState != GroundEnemyState.DEATH) return;
        cleanUpPhysicsOnDeath();
    }

    private void handleSensors(float dt){
        if(currentState == GroundEnemyState.DEATH){
            return;
        }
        if (facingRight) {
            if (sensors.rightWall > 0 || (sensors.rightCliffInitialized && sensors.rightCliff == 0)) {
                currentState = GroundEnemyState.TURN;
                facingRight = !facingRight;
                return;
            }
        } else {
            if (sensors.leftWall > 0 || (sensors.leftCliffInitialized && sensors.leftCliff == 0)) {
                currentState = GroundEnemyState.TURN;
                facingRight = !facingRight;
                return;
            }
        }
        currentState = GroundEnemyState.RUN;
    }

    public void handleAnimation(float delta){
        if(previousState == GroundEnemyState.TURN && !animation.create("turn", PlayMode.NORMAL, 0.1f).isAnimationFinished(stateTime)){
            currentState = GroundEnemyState.TURN;
        }
        if(previousState != currentState){
            stateTime = 0;
            previousState = currentState;
        }
        switch (currentState) {
            case GroundEnemyState.RUN:
                currentFrame = animation.create("Walk", PlayMode.LOOP, 0.1f).getKeyFrame(stateTime);
                break;
            case GroundEnemyState.DEATH:
                currentFrame = animation.create("Death Land", PlayMode.NORMAL, 0.1f).getKeyFrame(stateTime);
                break;
            case GroundEnemyState.TURN:
                currentFrame = animation.create("Turn", PlayMode.NORMAL, 0.05f).getKeyFrame(stateTime);
                break;
        
            default:
                break;
        }

        if(facingRight){
            currentFrame.flip(true, false);
        }
        if(currentState == GroundEnemyState.TURN){
            currentFrame.flip(true, false);
        }
        stateTime += delta;
    }

    public void handleVelacity(float delta){
        if(currentState == GroundEnemyState.DEATH || currentState == GroundEnemyState.TURN){
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            return;
        }
        float targetVelocity = facingRight ? speed : -speed;
        body.setLinearVelocity(targetVelocity, body.getLinearVelocity().y);
    }
    
    private void createBody(World world, Vector2 spawnPos) {
        //create main body
        
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.position.set(spawnPos.x / Constants.PPM, spawnPos.y / Constants.PPM);
        bdef.fixedRotation = true;
        body = world.createBody(bdef);
        body.setUserData(this);
        
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1f;
        fdef.friction = 0f;
        fdef.restitution = 0f;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_KNIGHT;

        PolygonShape shape = new PolygonShape();
        float hx = halfWidth / Constants.PPM;
        float hy = halfHeight / Constants.PPM;
        shape.setAsBox(hx, hy);
        fdef.shape = shape;
        fdef.isSensor = false;
        body.createFixture(fdef).setUserData("GroundEnemy_main_body");
        shape.dispose();

        sensors.createSensors(body, hx, hy);

    }

    private void cleanUpPhysicsOnDeath() {
        if (physicsCleanedUp) return;

        Filter disableFilter = new Filter();
        disableFilter.categoryBits = 0;
        disableFilter.maskBits = 0;

        Filter deadBodyFilter = new Filter();
        deadBodyFilter.categoryBits = Constants.BIT_DEAD_ENEMY;
        deadBodyFilter.maskBits = Constants.BIT_GROUND; 

        for (Fixture fixture : body.getFixtureList()) {
            if (fixture.isSensor()) {
                fixture.setFilterData(disableFilter);
            } else {
                fixture.setFilterData(deadBodyFilter);
            }
        }

        physicsCleanedUp = true;
    }
    
    @Override
    public void takeDamage(int how){
        this.health -= how;
        if(health <= 0){
            health = 0;
            currentState = GroundEnemyState.DEATH;
        }
    }
    @Override
    public void dispose() {
        
    }

    public GroundSurroundSensors getSensors() {
        return sensors;
    }

    public void setSensors(GroundSurroundSensors sensors) {
        this.sensors = sensors;
    }

    public int getDamage() {
        return damage;
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }

}
