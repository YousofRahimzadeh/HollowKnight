package Yousof.HollowKnight.Model.entities.knight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.AudioStore;
import Yousof.HollowKnight.Enum.CharmEnum;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightAttackSensors;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightScreamSensros;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightShadowDashSensors;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightSurroundSensors;
import Yousof.HollowKnight.Model.entities.knight.state.KnightDeathState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightIdleState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightKnockbackState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightOnSpikesState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightShadowDashState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightState;
import Yousof.HollowKnight.Utils.audio.AudioManager;

public class Knight extends Entitie {
    private int currentMasks = 5;
    private int currentSoul = 99;
    private final int maxSoul = 99;
    private final int maxMasks = 5;

    private int damage = 5; 
    private float maxSpeed = 3.0f;
    
    private KnightInventory inventory;
    private KnightState currentState;

    private KnightSurroundSensors surroundSensors;
    private KnightAttackSensors attackSensors;
    private KnightScreamSensros screamSensros;
    private KnightShadowDashSensors shadowDashSensors;

    private Vector2 lasPos;

    private boolean facingRight = true;
    private boolean onKnock = false;
    private boolean canDoubleJump = true;
    private boolean onDoubleJump = false;
    private boolean canDash = true;
    private boolean canMove = true;
    private boolean onGodMode = false;
    private boolean onSpectator = false;
    private float dashCooldwon = 0f;

    public Knight(World world, Vector2 spawnPos) {
        surroundSensors = new KnightSurroundSensors();
        attackSensors = new KnightAttackSensors();
        screamSensros = new KnightScreamSensros();
        shadowDashSensors = new KnightShadowDashSensors();
        inventory = new KnightInventory();
        createBody(world, spawnPos);
        changeState(new KnightIdleState());
        lasPos = body.getPosition();
    }

    public void update(float delta) {
        currentState.update(delta);
        if(!canDash && dashCooldwon > 0f){
            dashCooldwon -= delta;
            if(dashCooldwon <= 0f){
                dashCooldwon = 0f;
                canDash = true;
            }
        }
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
        fdef.filter.maskBits = Constants.BIT_ENEMY | Constants.BIT_GROUND | Constants.BIT_NPC;
        body.createFixture(fdef).setUserData("Knight_main_body");
        shape.dispose();

        surroundSensors.createSensors(body, hx, hy);
        attackSensors.createSensors(body, hx, hy);
        screamSensros.createSensors(body, hx, hy);
        shadowDashSensors.createSensors(body, hx, hy);
    }

    public void takeDamage(Enemy enemy){
        if(onGodMode) return;
        if(onKnock) return;
        
        this.currentMasks -= 1;
        if(currentMasks <= 0){
            currentMasks = 0;
            changeState(new KnightDeathState());
        }else{
            changeState(new KnightKnockbackState(enemy.getBody() , this , currentState , 6f));
        }
    }

    public void takeDamage(Body body){
        if(onGodMode) return;
        if(onKnock) return;
        
        this.currentMasks -= 1;
        if(currentMasks <= 0){
            currentMasks = 0;
            changeState(new KnightDeathState());
        }else{
            changeState(new KnightOnSpikesState());
            changeState(new KnightKnockbackState(body , this , currentState , 6f));
        }
    }

    public void takeDamage(Body body ,int how){
        if(onGodMode) return;
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
        AudioManager.getInstance().playSound(AudioStore.HollowKnightSoulGain.path);
        currentSoul += 11;
        if(inventory.isEquipped(CharmEnum.SOUL_CATCHER)) currentSoul += 4;
        if(currentSoul >= 99) currentSoul = 99;
    }

    public void addMaskRemoveSoul(){
        if(currentMasks == maxMasks) return;
        if(currentSoul < 33) return;

        currentSoul -= 33;
        currentMasks += 1;
        AudioManager.getInstance().playSound(AudioStore.HollowKnightHealthHeal.path);
    }

    public void dispose() {
        body.getWorld().destroyBody(body);
    }

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
    public void startDashCooldown() {
        canDash = false;
        dashCooldwon = inventory.isEquipped(CharmEnum.DASHMASTER) ? 0.1f : 1f;
    }
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
    public boolean isCanMove() {
        return canMove;
    }
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    public KnightInventory getInventory() {
        return inventory;
    }
    public void setInventory(KnightInventory inventory) {
        this.inventory = inventory;
    }
    public KnightState getCurrentState() {
        return currentState;
    }
    public boolean isOnGodMode() {return onGodMode;}
    public void setOnGodMode(boolean onGodMode) {this.onGodMode = onGodMode;}
    public boolean isOnSpectator() {return onSpectator;}
    public void setOnSpectator(boolean onSpectator) {this.onSpectator = onSpectator;}
    public Vector2 getLasPos() {return lasPos;}
    public void setLasPos(Vector2 lasPos) {this.lasPos = lasPos;}
    public void setCurrentState(KnightState currentState) {this.currentState = currentState;}
    public boolean isStrikingWithSharpShadow() {
        return this.currentState instanceof KnightShadowDashState;
    }
}