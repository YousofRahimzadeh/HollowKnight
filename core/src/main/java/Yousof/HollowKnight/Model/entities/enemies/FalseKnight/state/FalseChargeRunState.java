package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Utils.CameraSession;
import Yousof.HollowKnight.Utils.state.CameraVibrationState;

public class FalseChargeRunState extends FalseKnightState {

    private boolean theFirst = true;
    private Vector2 targetPosition;
    @Override
    public void enter(FalseKnightEnemy enemy) {
        super.enter(enemy);
        currentAnimation = Animations.FalseKnight.create("Run Antic", PlayMode.NORMAL, enemy.frameDuration);
        targetPosition = new Vector2(enemy.getFarSensors().knight.getBody().getPosition().x , enemy.getFarSensors().knight.getBody().getPosition().y);
        reCreateBody();
        CameraSession.getInstance().changeState(new CameraVibrationState(1f, 13f));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (currentAnimation.isAnimationFinished(stateTime) && theFirst) {
            stateTime = 0f;
            currentAnimation = Animations.FalseKnight.create("Run", PlayMode.LOOP, enemy.frameDuration);
            theFirst = false;
            return;
        }

        if(body.getPosition().x < targetPosition.x && !enemy.isFacingRight()) {
            enemy.changeState(new FalseIdleState());
            return;
        } else if(body.getPosition().x > targetPosition.x && enemy.isFacingRight()) {
            enemy.changeState(new FalseIdleState());
            return;

        }

        float speed = 3f * enemy.factor;
        if(enemy.isFacingRight()) {
            body.setLinearVelocity(speed, body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(-speed, body.getLinearVelocity().y);
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
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_KNIGHT | Constants.BIT_PROJECTILE;
        body.createFixture(fdef).setUserData("Enemy_main_body");

        enemy.getNearbySensors().createSensors(body, bodyHx, bodyHy);
        enemy.getFarSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getMiddleSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getGroundSensors().createSensors(body, bodyHx, bodyHy);
    }

    @Override
    public void exit() {
        
    }
}