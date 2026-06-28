package Yousof.HollowKnight.Model.entities.enemies.HuskHornhead;

import com.badlogic.gdx.graphics.g2d.Batch;
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
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.sensors.HuskSurroundSensors;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.state.HuskDeathState;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.state.HuskEnemyState;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.state.HuskKnockbackState;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.state.HuskRunState;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class HuskHornheadEnemy extends Enemy {
    private int health;
    private int damage = 5;

    private Animations animation;

    private float yOffset;

    private HuskEnemyState currentState;

    private final float speed;
    private final float halfWidth;
    private final float halfHeight;
    private boolean facingRight = true;
    private boolean physicsCleanedUp = false;

    private HuskSurroundSensors sensors;


    public HuskHornheadEnemy(World world, float x, float y, float width, float height, float speed , int health , Animations anim , float yOffset) {
        this.speed = speed;
        this.halfWidth = width / 2f;
        this.halfHeight = height / 2f;
        this.health = health;
        this.animation = anim;
        this.yOffset = yOffset;
        sensors = new HuskSurroundSensors();
        createBody(world, new Vector2(x, y));
        this.changeState(new HuskRunState());
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
    public void takeDamage(Knight knight){
        this.health -= knight.getDamage();
        if(health <= 0){
            health = 0;
            changeState(new HuskDeathState());
        }else{
            changeState(new HuskKnockbackState(knight.getBody(), currentState ,3f));
        }
    }

    @Override
    public void dispose() {
        
    }
    
    public void changeState(HuskEnemyState newState){
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
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_KNIGHT;

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

    public HuskSurroundSensors getSensors() {
        return sensors;
    }
    public void setSensors(HuskSurroundSensors sensors) {
        this.sensors = sensors;
    }
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public Animations getAnimation() {
        return animation;
    }
    public void setAnimation(Animations animation) {
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
