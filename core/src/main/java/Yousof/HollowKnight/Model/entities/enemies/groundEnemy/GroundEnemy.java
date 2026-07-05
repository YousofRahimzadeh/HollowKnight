package Yousof.HollowKnight.Model.entities.enemies.groundEnemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.sensors.GroundSurroundSensors;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.state.GroundDeathState;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.state.GroundEnemyState;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.state.GroundKnockbackState;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.state.GroundRunState;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class GroundEnemy extends Enemy {
    private int health;
    private int damage = 5;

    private AnimationManager animation;

    private float yOffset;

    private GroundEnemyState currentState;

    private final float speed;
    private final float halfWidth;
    private final float halfHeight;
    private boolean facingRight = true;
    private boolean physicsCleanedUp = false;

    private GroundSurroundSensors sensors;


    public GroundEnemy(World world, float x, float y, float width, float height, float speed , int health , AnimationManager anim , float yOffset) {
        this.speed = speed;
        this.halfWidth = width / 2f;
        this.halfHeight = height / 2f;
        this.health = health;
        this.animation = anim;
        this.yOffset = yOffset;
        sensors = new GroundSurroundSensors();
        createBody(world, new Vector2(x, y));
        this.changeState(new GroundRunState());
    }

    @Override
    public void update(float dt) {
        if(knockbackTimer > 0) {
            knockbackTimer -= dt;
            return;
        }
        currentState.update(dt);
    }

    @Override
    public void draw(Batch batch) {
        currentState.draw(batch);
    }

    @Override
    public void takeDamage(Body body , int how , float strength){
        this.health -= how;
        if(health <= 0){
            health = 0;
            changeState(new GroundDeathState());
        }else{
            changeState(new GroundKnockbackState(body, currentState ,strength));
        }
    }

    @Override
    public void dispose() {
        
    }
    
    public void changeState(GroundEnemyState newState){
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(this);
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
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_KNIGHT | Constants.BIT_PROJECTILE;

        PolygonShape shape = new PolygonShape();
        float hx = halfWidth / Constants.PPM;
        float hy = halfHeight / Constants.PPM;
        shape.setAsBox(hx, hy);
        fdef.shape = shape;
        fdef.isSensor = false;
        body.createFixture(fdef).setUserData("Enemy_main_body");
        shape.dispose();

        sensors.createSensors(body, hx, hy);

    }

    public void cleanUpPhysicsOnDeath() {
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
    public AnimationManager getAnimation() {
        return animation;
    }
    public void setAnimation(AnimationManager animation) {
        this.animation = animation;
    }
    public boolean isFacingRight() {
        return facingRight;
    }
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
    public float getyOffset() {
        return yOffset;
    }
    public float getSpeed() {
        return speed;
    }
}
