package Yousof.HollowKnight.Model.entities.knight;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import Yousof.HollowKnight.Enum.state.KnightState;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Keys;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightAttackSensors;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightSurroundSensors;
import Yousof.HollowKnight.Model.entities.knight.state.IKnightState;

public class KnightCopy extends Entitie {
    private int health;
    private int damage = 5;
    private float maxSpeed = 3.0f;
    private int timeOfJump = 0;

    private IKnightState state;
    
    private TextureRegion currentFrame;
    
    private KnightSurroundSensors surroundSensors;
    private KnightAttackSensors attackSensors;

    private boolean OnGround = true;
    private boolean OnWall = false;
    private boolean facingRight = true;
    private boolean canDoubleJump = true;
    private boolean canDash = true;
    
    private KnightState previousState;
    private KnightState currentState;
    private float stateTime = 0;

    private float dashSpeed = 12.0f;   
    private float dashDuration = 0.3f;
    private float dashTimer = 0;
    
    private float attackDuration = 0.35f;
    private float attackTimer = 0;
    
    public KnightCopy(World world, Vector2 spawnPos , int health) {
        this.health = health;
        surroundSensors = new KnightSurroundSensors();
        attackSensors = new KnightAttackSensors();
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
        drawKnight(batch);
        drawEffects(batch);
    }

    private void handleInput(float delta) {        
        
        OnGround = surroundSensors.downSensor > 0;
        if (OnGround) {
            canDoubleJump = true;
            canDash = true; 
        }

        if (!Gdx.input.isKeyPressed(Keys.KNIGHTJUMP.getKey())) {
            timeOfJump = 0;
        }

        if (currentState == KnightState.DEATH) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            return; 
        }

        if (currentState == KnightState.DASH) {
            dashTimer += delta; 
            if (dashTimer < dashDuration) {
                float direction = facingRight ? dashSpeed : -dashSpeed;
                body.setLinearVelocity(direction, 0);
                return; 
            } else {
                dashTimer = 0; 
                currentState = OnGround ? KnightState.IDLE : KnightState.FALL;
            }
        }

        if (Gdx.input.isKeyJustPressed(Keys.KNIGHTDASH.getKey()) && canDash) {
            currentState = KnightState.DASH;
            dashTimer = 0; 
            if (!OnGround) canDash = false;
            float direction = facingRight ? dashSpeed : -dashSpeed;
            body.setLinearVelocity(direction, 0);
            return;
        }

        if (currentState == KnightState.ATTACK || currentState == KnightState.ATTACKUP || currentState == KnightState.POGOJUMP) {
            attackTimer += delta;
            if (attackTimer < attackDuration) { 
                float moveX = 0;
                if (Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.getKey())) moveX = maxSpeed * 0.5f;
                else if (Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.getKey())) moveX = -maxSpeed * 0.5f;
                
                body.setLinearVelocity(moveX, body.getLinearVelocity().y);
                return; 
            } else {
                attackTimer = 0;
                currentState = OnGround ? KnightState.IDLE : KnightState.FALL;
            }
        }

        if (Gdx.input.isKeyJustPressed(Keys.KNIGHTATTACK.getKey())) {
            attackTimer = 0;
            if (Gdx.input.isKeyPressed(Keys.KNIGHTUP.getKey())) {
                currentState = KnightState.ATTACKUP;
            } else if (Gdx.input.isKeyPressed(Keys.KNIGHTDOWN.getKey()) && !OnGround) {
                currentState = KnightState.POGOJUMP;
            } else {
                currentState = KnightState.ATTACK;
            }
            performAttack();
            return;
        }

        boolean nearLeftWall = surroundSensors.leftSensor > 0;
        boolean nearRightWall = surroundSensors.rightSensor > 0; 

        if (!OnGround && (nearLeftWall || nearRightWall) && body.getLinearVelocity().y < 0) {
            boolean pressingTowardsWall = (nearLeftWall && Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.getKey())) ||
                                           (nearRightWall && Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.getKey()));
            if (pressingTowardsWall) {
                currentState = KnightState.WALLSIDE;
                body.setLinearVelocity(0, body.getLinearVelocity().y * 0.6f);
                facingRight = nearRightWall; 
                
                if (Gdx.input.isKeyJustPressed(Keys.KNIGHTJUMP.getKey())) {
                    currentState = KnightState.JUMP;
                    float kickForceX = nearLeftWall ? maxSpeed : -maxSpeed;
                    body.setLinearVelocity(kickForceX, maxSpeed * 1.2f);
                    facingRight = !nearLeftWall; 
                    timeOfJump = 1;
                    canDoubleJump = true; 
                }
                return;
            }
        }

        float targetVelocityX = 0;
        if (Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.getKey())) {
            targetVelocityX = maxSpeed;
            facingRight = true;
        } else if (Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.getKey())) {
            targetVelocityX = -maxSpeed;
            facingRight = false;
        }
        body.setLinearVelocity(targetVelocityX, body.getLinearVelocity().y);


        if (Gdx.input.isKeyJustPressed(Keys.KNIGHTJUMP.getKey())) {
            if (OnGround) {
                currentState = KnightState.JUMP;
                body.setLinearVelocity(body.getLinearVelocity().x, maxSpeed * 1.3f);
                timeOfJump = 1;
            } else if (canDoubleJump) {
                canDoubleJump = false;
                currentState = KnightState.JUMP;
                body.setLinearVelocity(body.getLinearVelocity().x, maxSpeed * 1.2f); 
                timeOfJump = 1;
            }
        } else if (Gdx.input.isKeyPressed(Keys.KNIGHTJUMP.getKey()) && timeOfJump > 0 && timeOfJump <= 25) {
            currentState = KnightState.JUMP;
            body.setLinearVelocity(body.getLinearVelocity().x, maxSpeed * 1.1f);
            timeOfJump++;
        }

        if (currentState != KnightState.JUMP || body.getLinearVelocity().y <= 0) {
            if (!OnGround && body.getLinearVelocity().y < -0.1f) {
                currentState = KnightState.FALL;
            } else if (OnGround) {
                if (Math.abs(body.getLinearVelocity().x) > 0.1f) {
                    currentState = KnightState.RUN;
                } else {
                    currentState = KnightState.IDLE;
                }
            }
        }
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
                currentFrame = Animations.Knight.create("Slash", Animation.PlayMode.NORMAL, 0.1f).getKeyFrame(stateTime, false);
                break;
            case ATTACKUP:
                currentFrame = Animations.Knight.create("UpSlash", Animation.PlayMode.NORMAL, 0.1f).getKeyFrame(stateTime, false);
                break;
            case POGOJUMP:
                currentFrame = Animations.Knight.create("DownSlash", Animation.PlayMode.NORMAL, 0.1f).getKeyFrame(stateTime, false);
                break;
            case DASH:
                currentFrame = Animations.Knight.create("Dash", Animation.PlayMode.LOOP_PINGPONG, 0.08f).getKeyFrame(stateTime, true);
                break;
            case DEATH:
                currentFrame = Animations.Knight.create("Death", Animation.PlayMode.NORMAL, 0.08f).getKeyFrame(stateTime, false);
                break;
        }

        if (currentFrame.isFlipX() != facingRight) {
            currentFrame.flip(true, false);
        }

        previousState = currentState;
        stateTime += delta;
    }

    private void drawKnight(Batch batch){
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 38;
        batch.draw(currentFrame, drawX, drawY);
    }

    private void drawEffects(Batch batch){
        if (currentState != KnightState.ATTACK && 
            currentState != KnightState.ATTACKUP && 
            currentState != KnightState.POGOJUMP &&
            currentState != KnightState.DASH) {
            return;
        }

        TextureRegion effectFrame = null;
        float effectX = 0;
        float effectY = 0;

        float knightCenterX = body.getPosition().x * Constants.PPM;
        float knightCenterY = body.getPosition().y * Constants.PPM;

        switch (currentState) {
            case ATTACK:
                effectFrame = Animations.KnightEffects.create("SlashEffect", Animation.PlayMode.NORMAL, 0.06f).getKeyFrame(attackTimer, false);

                float attackOffset = 45f; 

                if (facingRight) {
                    if (!effectFrame.isFlipX()) effectFrame.flip(true, false);
                    effectX = knightCenterX + attackOffset - (effectFrame.getRegionWidth() / 2f);
                } else {
                    if (effectFrame.isFlipX()) effectFrame.flip(true, false);
                    effectX = knightCenterX - attackOffset - (effectFrame.getRegionWidth() / 2f);
                }
                effectY = knightCenterY - (effectFrame.getRegionHeight() / 2f) + 10f; 
                break;

            case ATTACKUP:
                effectFrame = Animations.KnightEffects.create("UpSlashEffect", Animation.PlayMode.NORMAL, 0.06f).getKeyFrame(attackTimer, false);

                float upOffset = 65f; 
                effectX = knightCenterX - (effectFrame.getRegionWidth() / 2f);
                effectY = knightCenterY + upOffset - (effectFrame.getRegionHeight() / 2f);

                if (effectFrame.isFlipX() != facingRight) {
                    effectFrame.flip(true, false);
                }
                break;

            case POGOJUMP:
                effectFrame = Animations.KnightEffects.create("DownSlashEffect", Animation.PlayMode.NORMAL, 0.06f).getKeyFrame(attackTimer, false);

                float downOffset = 45f;
                effectX = knightCenterX - (effectFrame.getRegionWidth() / 2f);
                effectY = knightCenterY - downOffset - (effectFrame.getRegionHeight() / 2f);

                if (effectFrame.isFlipX() != facingRight) {
                    effectFrame.flip(true, false);
                }
                break;

            case DASH:
                Animation<TextureRegion> dashAnim = Animations.KnightEffects.create("Dash Effect", Animation.PlayMode.NORMAL, 0.06f);

                if (dashAnim.isAnimationFinished(dashTimer)) break;
            
                effectFrame = dashAnim.getKeyFrame(dashTimer, false);
                float dOffset = 250f;
            
                if (facingRight) {
                    if (effectFrame.isFlipX()) effectFrame.flip(true, false); 
                    effectX = knightCenterX - dOffset - (effectFrame.getRegionWidth() / 2f); 
                } else {
                    if (!effectFrame.isFlipX()) effectFrame.flip(true, false);
                    effectX = knightCenterX + dOffset - (effectFrame.getRegionWidth() / 2f);
                }
                effectY = knightCenterY - (effectFrame.getRegionHeight() / 2f); 
                break;
            default:
                break;
            }

        if (effectFrame != null) {
            batch.draw(effectFrame, effectX, effectY);
        }
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

    private void performAttack(){
        ArrayList<Enemy> enemies = null;
        if(currentState == KnightState.ATTACK){
            if(facingRight)
                enemies = attackSensors.rightSensor;
            else
                enemies = attackSensors.leftSensor;
        } else if(currentState == KnightState.ATTACKUP){
            enemies = attackSensors.upSensor;
        } else if(currentState == KnightState.POGOJUMP){
            enemies = attackSensors.downSensor;
            canDoubleJump = true;
        }
        
        if(enemies != null && !enemies.isEmpty()){
            for(Enemy enemy : enemies){
                if(enemy != null){
                    enemy.takeDamage(damage);
                }
            }
        }
    }

    public void takeDamage(int how){
        this.health -= how;
        if(health <= 0){
            health = 0;
            currentState = KnightState.DEATH;
        }
    }

    public void changeState(IKnightState newState){
        if (state != null) {
            state.exit();
        }
        state = newState;
    }

    public void dispose() {}

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
    public KnightSurroundSensors getSurroundSensors() {return surroundSensors;}
    public void setSurroundSensors(KnightSurroundSensors surroundSensors) {this.surroundSensors = surroundSensors;}
    public KnightAttackSensors getAttackSensors() {return attackSensors;}
    public void setAttackSensors(KnightAttackSensors attackSensors) {this.attackSensors = attackSensors;}
}