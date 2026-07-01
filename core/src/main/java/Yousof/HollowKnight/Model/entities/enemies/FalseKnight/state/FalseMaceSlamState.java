package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;

public class FalseMaceSlamState extends FalseKnightState {

    private boolean theFirst = true;
    private boolean theMiddle = true;
    private boolean theLast = true;
    @Override
    public void enter(FalseKnightEnemy enemy) {
        super.enter(enemy);
        currentAnimation = Animations.FalseKnight.create("Attack Antic", PlayMode.NORMAL, 0.1f);
        reCreateBody();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (currentAnimation.isAnimationFinished(stateTime) && theFirst) {
            stateTime = 0f;
            currentAnimation = Animations.FalseKnight.create("Attack", PlayMode.NORMAL, 0.1f);
            theFirst = false;
            return;
        }
        if (currentAnimation.isAnimationFinished(stateTime) && theMiddle) {
            performAttack();
            stateTime = 0f;
            currentAnimation = Animations.FalseKnight.create("Attack Recover", PlayMode.NORMAL, 0.1f);
            theMiddle = false;
            return;
        }
        if (currentAnimation.isAnimationFinished(stateTime) && theLast) {
            stateTime = 0f;
            enemy.changeState(new FalseIdleState());
            theLast = false;
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

        enemy.getNearbySensors().createSensors(body, bodyHx, bodyHy);
        enemy.getFarSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getMiddleSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getGroundSensors().createSensors(body, bodyHx, bodyHy);
    }

    public void performAttack() {
        if(enemy.getNearbySensors().knight == null) return;
        if(enemy.getNearbySensors().knight.getBody().getPosition().x > body.getPosition().x && !enemy.isFacingRight()) return;
        if(enemy.getNearbySensors().knight.getBody().getPosition().x < body.getPosition().x && enemy.isFacingRight()) return;

        enemy.getNearbySensors().knight.takeDamage(body,1);
    }

    @Override
    public void exit() {
        
    }
}