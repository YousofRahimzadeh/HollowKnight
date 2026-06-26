package Yousof.HollowKnight.Model.entities.knight;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Keys;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Enum.state.KnightState;
import Yousof.HollowKnight.Model.entities.Entitie;

public class Knight2 extends Entitie {
    private int health;
    private float maxSpeed = 3.0f;
    private int timeOfJump = 0;
    
    private TextureRegion currentFrame;
    private HashMap<KnightState , Animation<TextureRegion>> animations;
    
    private SurroundSensors surroundSensors;
    private AttackSensors attackSensors;

    private boolean OnGround = true;
    private boolean OnWall = false;
    private boolean facingRight = true;
    private boolean canDoubleJump = true;
    private KnightState previousState;
    private KnightState currentState;
    private float stateTime = 0;

    private float dashSpeed = 12.0f;   
    private float dashDuration = 0.4f;
    private float dashTimer = 0;
    
    public Knight2(World world, Vector2 spawnPos , int health) {
        this.health = health;
        surroundSensors = new SurroundSensors();
        attackSensors = new AttackSensors();
        previousState = KnightState.IDLE;
        currentState = KnightState.IDLE;
        currentFrame = Animations.Knight.create("Idle", Animation.PlayMode.LOOP , 0.08f).getKeyFrame(stateTime, true);
        createBody(world, spawnPos);
    }

    public void update(float delta) {
        handleInput(delta);
        handleAnimation(delta);
    }

    public void draw(Batch batch) {
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 28;
        batch.draw(currentFrame, drawX, drawY);
    }

    private void handleInput(float delta) {        

        if(!Gdx.input.isKeyPressed(Keys.KNIGHTJUMP.getKey())){
            timeOfJump = 0;
        }

        if(surroundSensors.downSensor > 0){
            canDoubleJump = true;
        }

        if (currentState == KnightState.DASH) {
            dashTimer += delta; 

            if (dashTimer < dashDuration) {
                float direction = facingRight ? dashSpeed : -dashSpeed;
                body.setLinearVelocity(direction, 0); 
                return;
            } else {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                currentState = KnightState.IDLE;
                dashTimer = 0; 
            }
        }

        if (currentState == KnightState.WALLSIDE) {

        }

        if(surroundSensors.leftSensor > 0 && surroundSensors.downSensor <= 0){
            if (Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.getKey())) {
                body.setLinearVelocity(0, body.getLinearVelocity().y * 0.7f);
                facingRight = true;
                currentState = KnightState.WALLSIDE;
            } else if (Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.getKey())) {
                body.setLinearVelocity(0, body.getLinearVelocity().y * 0.7f);
                facingRight = false;
                currentState = KnightState.WALLSIDE;
            } else {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                if (!OnGround) currentState = KnightState.FALL;
                else currentState = KnightState.IDLE;
            }
            return;
        }

        // X Axis
        if (Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.getKey())) {
            body.setLinearVelocity(maxSpeed, body.getLinearVelocity().y);
            facingRight = true;
            currentState = KnightState.RUN;
        } else if (Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.getKey())) {
            body.setLinearVelocity(-maxSpeed, body.getLinearVelocity().y);
            facingRight = false;
            currentState = KnightState.RUN;
        } else {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            if (OnGround) currentState = KnightState.IDLE;
        }

        // Jump
        if (Gdx.input.isKeyJustPressed(Keys.KNIGHTJUMP.getKey())) {
            if (OnGround) {
                currentState = KnightState.JUMP;
                body.setLinearVelocity(body.getLinearVelocity().x, maxSpeed);
                timeOfJump = 1;
            } else if (canDoubleJump) {
                canDoubleJump = false;
                currentState = KnightState.JUMP;
                body.setLinearVelocity(body.getLinearVelocity().x, maxSpeed); 
                timeOfJump = 1;
            }
        } else if (Gdx.input.isKeyPressed(Keys.KNIGHTJUMP.getKey()) && timeOfJump > 0 && timeOfJump <= 35) {
            currentState = KnightState.JUMP;
            body.setLinearVelocity(body.getLinearVelocity().x, maxSpeed);
            timeOfJump++;
        }

        // Attack
        if(Gdx.input.isKeyJustPressed(Keys.KNIGHTATTACK.getKey())){
            
        }
        // Dash
        if(Gdx.input.isKeyJustPressed(Keys.KNIGHTDASH.getKey())){
            currentState = KnightState.DASH;
            dashTimer = 0; 
            float direction = facingRight ? dashSpeed : -dashSpeed;
            body.setLinearVelocity(direction, 0);
        }

        if(!OnGround && body.getLinearVelocity().y < 0 && currentState != KnightState.DASH){
            currentState = KnightState.FALL;
        }else if(OnGround && body.getLinearVelocity().x < 0.001f && body.getLinearVelocity().x > -0.001f){
            currentState = KnightState.IDLE;
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
    }

    private void handleDash(){
        
    }

    private void handleAnimation(float delta){
        if(currentState != previousState){
            stateTime = 0;
        }
        switch (currentState) {
            case IDLE:
                currentFrame = Animations.Knight.create("Idle", Animation.PlayMode.LOOP , 0.08f).getKeyFrame(stateTime, true);
                break;
            case RUN:
                currentFrame = Animations.Knight.create("Run", Animation.PlayMode.LOOP , 0.08f).getKeyFrame(stateTime, true);
                break;
            case JUMP:
                currentFrame = Animations.Knight.create("Airborne", Animation.PlayMode.LOOP, 0.08f).getKeyFrame(stateTime, true);
                break;
            case FALL:
                currentFrame = Animations.Knight.create("Fall", Animation.PlayMode.LOOP_PINGPONG, 0.08f).getKeyFrame(stateTime, true);
                break;
            case WALLSIDE:
                currentFrame = Animations.Knight.create("Wall Slide", Animation.PlayMode.LOOP, 0.08f).getKeyFrame(stateTime, true);
                break;
            case ATTACK:
                currentFrame = Animations.Knight.create("Attack", Animation.PlayMode.LOOP, 0.08f).getKeyFrame(stateTime, true);
                break;
            case ATTACKUP:
                currentFrame = Animations.Knight.create("AttackUp", Animation.PlayMode.LOOP, 0.08f).getKeyFrame(stateTime, true);
                break;
            case POGOJUMP:
                currentFrame = Animations.Knight.create("PogoJump", Animation.PlayMode.LOOP, 0.08f).getKeyFrame(stateTime, true);
                break;
            case DASH:
                currentFrame = Animations.Knight.create("Dash", Animation.PlayMode.LOOP_PINGPONG, 0.08f).getKeyFrame(stateTime, true);
                break;
            case DEATH:
                currentFrame = Animations.Knight.create("Death", Animation.PlayMode.NORMAL, 0.08f).getKeyFrame(stateTime, true);
                break;
        }

        if (currentFrame.isFlipX() != facingRight) {
            currentFrame.flip(true, false);
        }

        previousState = currentState;
        stateTime += delta;
    }

    private void createBody(World world, Vector2 spawnPos){
        float hx = 16 / Constants.PPM;
        float hy = 60 / Constants.PPM;
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
        body.createFixture(fdef).setUserData("bounds");
        shape.dispose();

        surroundSensors.createSensors(body, hx, hy);
        attackSensors.createSensors(body, hx, hy);
    }

    public void takeDamage(int how){
        this.health -= how;
        if(health <= 0){
            health = 0;
            currentState = KnightState.DEATH;
        }
    }

    public void dispose() {  

    }

    public boolean isOnWall() {return OnWall;}
    public void setOnWall(boolean onWall) {OnWall = onWall;}
    public boolean isOnGround() {return OnGround;}
	public void setOnGround(boolean OnGround) {this.OnGround = OnGround;}

    public Vector2 getPos() { return body.getPosition(); }
    public void setPos(Vector2 pos) { body.setTransform(pos, body.getAngle()); }
    public Vector2 getVel() { return body.getLinearVelocity(); }
    public void setVel(Vector2 vel) { body.setLinearVelocity(vel); }
    public float getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }
    public SurroundSensors getSurroundSensors() {return surroundSensors;}
    public void setSurroundSensors(SurroundSensors surroundSensors) {this.surroundSensors = surroundSensors;}
    public AttackSensors getAttackSensors() {return attackSensors;}
    public void setAttackSensors(AttackSensors attackSensors) {this.attackSensors = attackSensors;}
}
