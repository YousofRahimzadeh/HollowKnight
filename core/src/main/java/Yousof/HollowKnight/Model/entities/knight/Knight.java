package Yousof.HollowKnight.Model.entities.knight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightAttackSensors;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightSurroundSensors;
import Yousof.HollowKnight.Model.entities.knight.state.KnightDeathState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightIdleState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightKnockbackState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightState;

public class Knight extends Entitie {
    private int health;
    private int soul = 0;
   
    private int damage = 5; 
    private float maxSpeed = 3.0f;

    private KnightState currentState;
    
    private KnightSurroundSensors surroundSensors;
    private KnightAttackSensors attackSensors;

    private boolean facingRight = true;
    private boolean onKnock = false;
    private boolean canDoubleJump = true;
    private boolean canDash = true;


    public Knight(World world, Vector2 spawnPos , int health) {
        this.health = health;
        surroundSensors = new KnightSurroundSensors();
        attackSensors = new KnightAttackSensors();
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
    }

    public void takeDamage(Enemy enemy){
        if(onKnock) return;
        
        this.health -= 1;
        if(health <= 0){
            health = 0;
            changeState(new KnightDeathState());
        }else{
            changeState(new KnightKnockbackState(enemy.getBody() , currentState , 6f));
        }
    }

    public void changeState(KnightState newState){
        if (currentState != null) {
            currentState.exit();
        }
        if(onKnock){
            ((KnightKnockbackState)currentState).changeState(newState);
            return;
        }
        currentState = newState;
        currentState.enter(this);
    }

    public void dispose() {}


    public boolean isOnKnock() {return onKnock;}
    public void setOnKnock(boolean onKnock) {this.onKnock = onKnock;}
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
    public boolean isCanDoubleJump() {
        return canDoubleJump;
    }
    public void setCanDoubleJump(boolean canDoubleJump) {
        this.canDoubleJump = canDoubleJump;
    }
    public boolean isCanDash() {
        return canDash;
    }
    public void setCanDash(boolean canDash) {this.canDash = canDash;}
    
    
    public int getSoul() {return soul;}
    public void addSoul() {soul += 11;}
    public void setSoul(int soul) {this.soul = soul;}
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
}