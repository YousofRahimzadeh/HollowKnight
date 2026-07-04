package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Utils.animation.AnimationManager;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.camera.state.CameraVibrationState;

public class FalseDefensiveLeapState extends FalseKnightState {

    private enum LeapPhase { JUMPING, LANDING}
    private LeapPhase currentPhase;
    @Override
    public void enter(FalseKnightEnemy enemy) {
        super.enter(enemy);
        
        currentAnimation = AnimationManager.FalseKnight.create("Jump", PlayMode.NORMAL, enemy.frameDuration);
        currentPhase = LeapPhase.JUMPING;

        launchLeapWithImpulse();
        CameraSession.getInstance().changeState(new CameraVibrationState(1f, 13f));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(firstUpdate){
            reCreateBody();
            firstUpdate = false;
        }

        if (currentPhase == LeapPhase.JUMPING && enemy.getGroundSensors().groundSensor > 0 && currentAnimation.isAnimationFinished(stateTime)) {
            currentPhase = LeapPhase.LANDING;
            body.setLinearVelocity(0 , body.getLinearVelocity().y);
            currentAnimation = AnimationManager.FalseKnight.create("Land", PlayMode.NORMAL, enemy.frameDuration);
            stateTime = 0f;
            return;
        } 

        if (currentPhase == LeapPhase.LANDING && currentAnimation.isAnimationFinished(stateTime)) {
            enemy.changeState(new FalseIdleState());
            return;
        }
    }

    private void launchLeapWithImpulse() {
        float impulseY = 50f; 
        float impulseX = 15f;  

        if (enemy.getFarSensors().knight != null) {
            float knightX = enemy.getFarSensors().knight.getBody().getPosition().x;
            float bossX = body.getPosition().x;

            if (knightX > bossX) {
                impulseX = -impulseX;
            }
        } else {
            if (enemy.isFacingRight()) {
                impulseX = -impulseX;
            }
        }

        body.setLinearVelocity(0f, 0f);
        body.applyLinearImpulse(new Vector2(impulseX, impulseY), body.getWorldCenter(), true);
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

        enemy.getNearbySensors().createSensors(body, bodyHx, bodyHy);
        enemy.getFarSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getMiddleSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getGroundSensors().createSensors(body, bodyHx, bodyHy);
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
}