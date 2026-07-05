package Yousof.HollowKnight.Model.entities.enemies.FalseKnight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.sensors.FalseFarSensors;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.sensors.FalseSurroundSensors;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.sensors.FalseMiddleSensors;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.sensors.FalseNearbySensors;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state.FalseDeathState;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state.FalseIdleState;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state.FalseKnightState;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state.FalseKnockbackState;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state.FalseStunState;

public class FalseKnightEnemy extends Enemy{
    
    private int health = 100;
    private int damage = 5;
    private float speed = 10f;
    private FalseKnightState currentState;
    private String lastPerformedMove = "";

    private boolean isPhaseTwo = false;
    public float factor = 1.0f;
    public float frameDuration = 0.1f;

    private boolean facingRight = true;
    private boolean onKnock = false;
    private boolean physicsCleanedUp = false;

    private FalseNearbySensors nearbySensors;
    private FalseMiddleSensors middleSensors;
    private FalseFarSensors farSensors;
    private FalseSurroundSensors groundSensors;

    public FalseKnightEnemy(World world, float x, float y) {
        nearbySensors = new FalseNearbySensors();
        middleSensors = new FalseMiddleSensors();
        farSensors = new FalseFarSensors();
        groundSensors = new FalseSurroundSensors();
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
    public void takeDamage(Body body, int how , float strength) {
        this.health -= how;
    
        if (health <= 0 && !(currentState instanceof FalseDeathState)) {
            health = 0;
            changeState(new FalseDeathState());
            return;
        }
    
        if (health <= 50 && !isPhaseTwo) {
            isPhaseTwo = true;
            factor = 2.0f;
            frameDuration /= factor;
            changeState(new FalseStunState());
            return;
        }
    
        if (currentState instanceof FalseStunState) {
            ((FalseStunState) currentState).currentPhase = FalseStunState.LeapPhase.HITING;
        }else if(currentState instanceof FalseDeathState){
            return;
        } else {
            changeState(new FalseKnockbackState(body, currentState, strength));
        }
    }

    @Override
    public void dispose() {
        
    }
    
    public void changeState(FalseKnightState newState){
        if (onKnock && currentState instanceof FalseKnockbackState) {
            ((FalseKnockbackState) currentState).changeState(newState);
            return;
        }
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        if(!currentState.isEntered()) currentState.enter(this);
    }
    
    private void createBody(World world, Vector2 spawnPos) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.position.set(spawnPos.x / Constants.PPM, spawnPos.y / Constants.PPM);
        bdef.fixedRotation = true;
        body = world.createBody(bdef);
        body.setUserData(this);

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

    
    public FalseNearbySensors getNearbySensors() {
        return nearbySensors;
    }
    public void setNearbySensors(FalseNearbySensors gorzSensors) {
        this.nearbySensors = gorzSensors;
    }
    public FalseMiddleSensors getMiddleSensors() {
        return middleSensors;
    }
    public void setMiddleSensors(FalseMiddleSensors middleSensors) {
        this.middleSensors = middleSensors;
    }
    public FalseFarSensors getFarSensors() {
        return farSensors;
    }
    public void setFarSensors(FalseFarSensors surroundSensors) {
        this.farSensors = surroundSensors;
    }
    public FalseSurroundSensors getGroundSensors() {
        return groundSensors;
    }

    public void setGroundSensors(FalseSurroundSensors groundSensors) {
        this.groundSensors = groundSensors;
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

    public boolean isOnKnock() {
        return onKnock;
    }

    public void setOnKnock(boolean onKnock) {
        this.onKnock = onKnock;
    }
    public String getLastPerformedMove() { return lastPerformedMove; }
    public void setLastPerformedMove(String move) { this.lastPerformedMove = move; }
}