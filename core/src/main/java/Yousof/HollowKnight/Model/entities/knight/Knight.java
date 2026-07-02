package Yousof.HollowKnight.Model.entities.knight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightAttackSensors;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightScreamSensros;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightSurroundSensors;
import Yousof.HollowKnight.Model.entities.knight.state.KnightDeathState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightIdleState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightKnockbackState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightState;

public class Knight extends Entitie {
    private int currentMasks = 5;
    private int currentSoul = 99;
    private final int maxSoul = 99;
    private final int maxMasks = 5;

    private int damage = 5; 
    private float maxSpeed = 3.0f;

    private KnightState currentState;
    
    private KnightSurroundSensors surroundSensors;
    private KnightAttackSensors attackSensors;
    private KnightScreamSensros screamSensros;

    private float focusDuration = 1.5f;

    private boolean facingRight = true;
    private boolean onKnock = false;
    private boolean canDoubleJump = true;
    private boolean onDoubleJump = false;
    private boolean canDash = true;


    public Knight(World world, Vector2 spawnPos) {
        surroundSensors = new KnightSurroundSensors();
        attackSensors = new KnightAttackSensors();
        screamSensros = new KnightScreamSensros();
        createBody(world, spawnPos);
        changeState(new KnightIdleState());
    }

    public void update(float delta) {
        currentState.update(delta);
        
    }

    public void draw(Batch batch) {
        currentState.draw(batch);
    }

    private void createBody(World world, Vector2 spawnPos){
        float hx = 14 / Constants.PPM;
        float hy = 50 / Constants.PPM;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(spawnPos.x / Constants.PPM, spawnPos.y / Constants.PPM);
        body = world.createBody(bdef);
        body.setUserData(this);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy);
        fdef.shape = shape;
        fdef.friction = 0f;
        fdef.filter.categoryBits = Constants.BIT_KNIGHT;
        fdef.filter.maskBits = Constants.BIT_ENEMY | Constants.BIT_GROUND;
        body.createFixture(fdef).setUserData("Knight_main_body");
        shape.dispose();

        surroundSensors.createSensors(body, hx, hy);
        attackSensors.createSensors(body, hx, hy);
        screamSensros.createSensors(body, hx, hy);
    }

    public void takeDamage(Enemy enemy){
        if(onKnock) return;
        
        this.currentMasks -= 1;
        if(currentMasks <= 0){
            currentMasks = 0;
            changeState(new KnightDeathState());
        }else{
            changeState(new KnightKnockbackState(enemy.getBody() , this , currentState , 6f));
        }
    }

    public void takeDamage(){
        if(onKnock) return;
        
        this.currentMasks -= 1;
        if(currentMasks <= 0){
            currentMasks = 0;
            changeState(new KnightDeathState());
        }
    }

    public void takeDamage(Body body ,int how){
        if(onKnock) return;
        
        this.currentMasks -= how;
        if(currentMasks <= 0){
            currentMasks = 0;
            changeState(new KnightDeathState());
        }else{
            changeState(new KnightKnockbackState(body , this , currentState , 6f));
        }
    }

    public void changeState(KnightState newState){
        if(onKnock){
            ((KnightKnockbackState)currentState).changeState(newState);
            return;
        }
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(this);
    }

    public void addCurrentSoul(){
        if(currentSoul >= 99) return;
        currentSoul += 11;
    }

    public void addMaskRemoveSoul(){
        if(currentMasks == maxMasks) return;
        if(currentSoul < 33) return;

        currentSoul -= 33;
        currentMasks += 1;
    }

    public void dispose() {}

    public boolean isOnKnock() {return onKnock;}
    public void setOnKnock(boolean onKnock) {this.onKnock = onKnock;}
    public int getDamage() {return damage;}
    public void setDamage(int damage) {this.damage = damage;}
    public boolean isFacingRight() {return facingRight;}
    public void setFacingRight(boolean facingRight) {this.facingRight = facingRight;}
    public boolean isCanDoubleJump() {return canDoubleJump;}
    public void setCanDoubleJump(boolean canDoubleJump) {this.canDoubleJump = canDoubleJump;}
    public boolean isOnDoubleJump() {return onDoubleJump;}
    public void setOnDoubleJump(boolean onDoubleJump) {this.onDoubleJump = onDoubleJump;}
    public boolean isCanDash() {return canDash;}
    public void setCanDash(boolean canDash) {this.canDash = canDash;}
    public float getFocusDuration() {return focusDuration;}
    public void setFocusDuration(float focousDuration) {this.focusDuration = focousDuration;}
    public int getMaxSoul() {return maxSoul;}
    public int getMaxMasks() {return maxMasks;}
    public int getCurrentMasks() {return currentMasks;}
    public void setCurrentMasks(int currentMasks) {this.currentMasks = currentMasks;}
    public int getCurrentSoul() {return currentSoul;}
    public void setCurrentSoul(int currentSoul) {this.currentSoul = currentSoul;}
    public Vector2 getPos() { return body.getPosition(); }
    public void setPos(Vector2 pos) { body.setTransform(pos, body.getAngle()); }
    public Vector2 getVel() { return body.getLinearVelocity(); }
    public void setVel(Vector2 vel) { body.setLinearVelocity(vel); }
    public float getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }
    public KnightSurroundSensors getSurroundSensors() {return surroundSensors;}
    public void setSurroundSensors(KnightSurroundSensors surroundSensors) {this.surroundSensors = surroundSensors;}
    public KnightAttackSensors getAttackSensors() {return attackSensors;}
    public void setAttackSensors(KnightAttackSensors attackSensors) {this.attackSensors = attackSensors;}
    public KnightScreamSensros getScreamSensros() {return screamSensros;}
    public void setScreamSensros(KnightScreamSensros screamSensros) {this.screamSensros = screamSensros;}
}