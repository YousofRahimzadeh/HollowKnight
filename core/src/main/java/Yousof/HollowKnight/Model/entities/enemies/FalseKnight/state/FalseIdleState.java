package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;

public class FalseIdleState extends FalseKnightState {

    private boolean directionalDecisionMade = false;
    private float duration = 1.5f;

    @Override
    public void enter(FalseKnightEnemy enemy) {
        super.enter(enemy);
        currentAnimation = Animations.FalseKnight.create("Idle", PlayMode.LOOP, enemy.frameDuration);
        directionalDecisionMade = false;
        
        duration /= enemy.factor; 
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(firstUpdate){
            reCreateBody();
            firstUpdate = false;
        }

        body.setLinearVelocity(0f, body.getLinearVelocity().y);
        
        if(stateTime < duration) return;

        if(!directionalDecisionMade && enemy.getFarSensors().knight != null){
            Vector2 knightPos = enemy.getFarSensors().knight.getBody().getPosition();
            if(knightPos.x > body.getPosition().x && !enemy.isFacingRight()) {
                enemy.setFacingRight(true);
            } else if(knightPos.x < body.getPosition().x && enemy.isFacingRight()) {
                enemy.setFacingRight(false);
            }
            directionalDecisionMade = true;
            return; 
        }
        
        String lastMove = enemy.getLastPerformedMove(); 
        float rand = MathUtils.random();

        if(enemy.getNearbySensors().knight != null) {
            if ("MaceSlam".equals(lastMove)) {
                navigateToState(new FalseDefensiveLeapState(), "DefensiveLeap");
            } else {
                navigateToState(new FalseMaceSlamState(), "MaceSlam");
            }
            return;
        }

        boolean inMiddle = enemy.getMiddleSensors().knight != null;
        boolean inFar = enemy.getFarSensors().knight != null;

        if (inMiddle || inFar) {

            if (inMiddle) {
                if (rand < 0.60f) {
                    if ("OffensiveLeap".equals(lastMove)) {
                        navigateToState(new FalseChargeRunState(), "ChargeRun");
                    } else {
                        navigateToState(new FalseOffensiveLeapState(), "OffensiveLeap");
                    }
                } else {
                    if ("ChargeRun".equals(lastMove)) {
                        navigateToState(new FalseOffensiveLeapState(), "OffensiveLeap");
                    } else {
                        navigateToState(new FalseChargeRunState(), "ChargeRun");
                    }
                }
            } 
            else if (inFar) {
                if (rand < 0.70f) {
                    if ("ChargeRun".equals(lastMove)) {
                        if (enemy.factor > 1.0f) {
                            navigateToState(new FalseChargeMaceSlamState(), "ChargeMaceSlam");
                        } else {
                            navigateToState(new FalseOffensiveLeapState(), "OffensiveLeap");
                        }
                    } else {
                        navigateToState(new FalseChargeRunState(), "ChargeRun");
                    }
                } else {
                    if (enemy.factor > 1.0f && !"ChargeMaceSlam".equals(lastMove)) {
                        navigateToState(new FalseChargeMaceSlamState(), "ChargeMaceSlam");
                    } else {
                        navigateToState(new FalseOffensiveLeapState(), "OffensiveLeap");
                    }
                }
            }
            return;
        }
    }

    private void navigateToState(FalseKnightState nextState, String moveName) {
        enemy.setLastPerformedMove(moveName);
        enemy.changeState(nextState);
    }

    @Override
    public void draw(Batch batch) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        if (enemy.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!enemy.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 130f;
        batch.draw(currentFrame, drawX, drawY);
        drawEffects(batch, stateTime);
    }

    public void reCreateBody(){
        super.reCreateBody();
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1f;
        fdef.friction = 0f;
        fdef.restitution = 0f;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_KNIGHT;

        float bodyHx = (250f / Constants.PPM) / 2f;
        float bodyHy = (260f / Constants.PPM) / 2f;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bodyHx, bodyHy);
        fdef.shape = shape;
        fdef.isSensor = false;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_KNIGHT | Constants.BIT_PROJECTILE;
        body.createFixture(fdef).setUserData("Enemy_main_body");

        shape.dispose();

        enemy.getNearbySensors().createSensors(body, bodyHx, bodyHy);
        enemy.getFarSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getMiddleSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getGroundSensors().createSensors(body, bodyHx, bodyHy);
    }
    
    @Override
    public void exit() {
        
    }
}