package Yousof.HollowKnight.Model.entities.knight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightAttackSensors;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightSurroundSensors;
import Yousof.HollowKnight.Model.entities.knight.state.IKnightState;
import Yousof.HollowKnight.Model.entities.knight.state.IdleState;

public class Knight extends Entitie {
    private int health;
    private int damage = 5;
    private float maxSpeed = 3.0f;

    private IKnightState currentState;
    
    private KnightSurroundSensors surroundSensors;
    private KnightAttackSensors attackSensors;

    private boolean OnGround = true;
    private boolean OnWall = false;
    private boolean facingRight = true;
    private boolean canDoubleJump = true;
    private boolean canDash = true;


    public Knight(World world, Vector2 spawnPos , int health) {
        this.health = health;
        surroundSensors = new KnightSurroundSensors();
        attackSensors = new KnightAttackSensors();
        createBody(world, spawnPos);
        changeState(new IdleState());
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

    public void takeDamage(int how){
        this.health -= how;
        if(health <= 0){
            health = 0;
            changeState(currentState);
        }
    }

    public void changeState(IKnightState newState){
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(this);
    }

    public void dispose() {}



    public boolean isOnWall() {return OnWall;}
    public void setOnWall(boolean onWall) {OnWall = onWall;}
    public boolean isOnGround() {return OnGround;}
    public void setOnGround(boolean OnGround) {this.OnGround = OnGround;}
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