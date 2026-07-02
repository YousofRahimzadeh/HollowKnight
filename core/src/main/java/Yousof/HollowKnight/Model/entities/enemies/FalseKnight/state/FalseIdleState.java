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

    @Override
    public void enter(FalseKnightEnemy enemy) {
        super.enter(enemy);
        currentAnimation = Animations.FalseKnight.create("Idle", PlayMode.LOOP, 0.1f);
        directionalDecisionMade = false;
        reCreateBody();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        body.setLinearVelocity(0f, body.getLinearVelocity().y);
        
        if(stateTime < 1.5f) return;

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

        if(enemy.getNearbySensors().knight != null) {
            enemy.changeState(new FalseMaceSlamState());
            return;
        }

        boolean inMiddle = enemy.getMiddleSensors().knight != null;
        boolean inFar = enemy.getFarSensors().knight != null;

        if (inMiddle || inFar) {

            float rand = MathUtils.random(); 

            if (inMiddle) {

                if (rand < 0.60f) {
                    enemy.changeState(new FalseOffensiveLeapState());
                } else {
                    enemy.changeState(new FalseChargeRunState());
                }
            } 
            else if (inFar) {

                if (rand < 0.70f) {
                    enemy.changeState(new FalseChargeRunState());
                } else {
                    enemy.changeState(new FalseOffensiveLeapState());
                }
            }
            return;
        }
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
        body.createFixture(fdef).setUserData("Enemy_main_body");

        float headHx = (125f / Constants.PPM) / 2f;
        float headHy = (80f / Constants.PPM) / 2f;
        shape.setAsBox(headHx, headHy , new Vector2(0f , bodyHy + headHy) , 0f);
        fdef.shape = shape;
        fdef.isSensor = false;
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