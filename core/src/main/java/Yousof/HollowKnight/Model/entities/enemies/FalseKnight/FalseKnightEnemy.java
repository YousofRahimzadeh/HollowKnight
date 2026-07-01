package Yousof.HollowKnight.Model.entities.enemies.FalseKnight;

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
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state.FalseIdleState;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state.FalseKnightState;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.sensors.GroundSurroundSensors;

public class FalseKnightEnemy extends Enemy{
    
    private int health = 100;
    private int damage = 5;
    private float speed = 10f;
    private FalseKnightState currentState;

    private boolean facingRight = true;
    private boolean physicsCleanedUp = false;

    private GroundSurroundSensors sensors;


    public FalseKnightEnemy(World world, float x, float y) {
        sensors = new GroundSurroundSensors();
        createBody(world, new Vector2(x, y));
        this.changeState(new FalseIdleState());
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
    public void takeDamage(Body body , int how){
        this.health -= how;
        if(health <= 0){
            health = 0;
            // changeState(new GroundDeathState());
        }else{
            // changeState(new GroundKnockbackState(body, currentState ,3f));
        }
    }

    @Override
    public void dispose() {
        
    }
    
    public void changeState(FalseKnightState newState){
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
        float hx = 250f / Constants.PPM;
        float hy = 260f / Constants.PPM;
        shape.setAsBox(hx, hy);
        fdef.shape = shape;
        fdef.isSensor = false;
        body.createFixture(fdef).setUserData("Enemy_main_body");
        shape.dispose();

        // sensors.createSensors(body, hx, hy);

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
    public boolean isFacingRight() {
        return facingRight;
    }
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
    public float getSpeed() {
        return speed;
    }
}