package Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy;

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
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.sensors.WingedSentrySensor;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state.WingedDeathState;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state.WingedIdleState;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state.WingedKnockbackState;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state.WingedSentryState;

public class WingedSentry extends Enemy{
    private int health = 100;

    private final float speed = 1f;
    private final float halfWidth = 50f;
    private final float halfHeight = 75f;
    private final float yOffset = -70f;
    private final float xOffset = -30f;

    private boolean facingRight = true;

    private WingedSentryState currentState;
    private WingedSentrySensor sensors;

    public WingedSentry(World world, float x, float y){
        sensors = new WingedSentrySensor();
        createBody(world, new Vector2(x, y));
        changeState(new WingedIdleState());
    }

    @Override
    public void update(float dt) {
        currentState.update(dt);
    }

    @Override
    public void draw(Batch batch) {
        currentState.draw(batch);
    }

    @Override
    public void takeDamage(Body body , int how , float strength){
        if(health == 0){
            return;
        }
        health -= how;
        if(health <= 0){
            health = 0;
            changeState(new WingedDeathState());
        }else{
            changeState(new WingedKnockbackState(body, currentState ,strength));
        }
    }

    @Override
    public void dispose() {
        
    }

    private void createBody(World world, Vector2 spawnPos) {
        //create main body
        
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.gravityScale = 0f;
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

    public void changeState(WingedSentryState newState){
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(this);
    }

    public void cleanUpPhysicsOnDeath() {

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

    }

    public WingedSentrySensor getSensor() {
        return sensors;
    }

    public float getSpeed() {
        return speed;
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
    public float getxOffset() {
        return xOffset;
    }

}
