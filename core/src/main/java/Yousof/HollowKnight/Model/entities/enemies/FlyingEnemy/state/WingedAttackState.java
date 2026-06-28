package Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.WingedSentry;

public class WingedAttackState extends WingedSentryState{

    private Animation<TextureRegion> currentAnimation;
    private float targetX;
    private boolean isRight;
    private boolean isChargingFinish = false;
    @Override
    public void enter(WingedSentry enemy) {
        super.enter(enemy);
        currentAnimation = Animations.WingedSentry.create("Charge Antic", PlayMode.NORMAL, 0.09f);
        body.setLinearVelocity(0, 0);
        targetX = enemy.getSensor().knight.getBody().getPosition().x;
        isRight = (targetX > body.getPosition().x);
        enemy.setFacingRight(isRight);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(!isChargingFinish){
            if(currentAnimation.isAnimationFinished(stateTime)){
                currentAnimation = Animations.WingedSentry.create("Charge", PlayMode.LOOP, 0.08f);
                isChargingFinish = true;
                stateTime = 0;
            }else {
                body.setLinearVelocity(0, 0);
                return;
            }
        }

        float attackSpeed = enemy.getSpeed() * 3f;

        if(isRight){
            if(targetX < body.getPosition().x || stateTime > 5f){
                enemy.changeState(new WingedIdleState());
                return;
            }
            body.setLinearVelocity(attackSpeed, 0);     
        }else{
            if(targetX > body.getPosition().x || stateTime > 5f){
                enemy.changeState(new WingedIdleState());
                return;
            }
            body.setLinearVelocity(-attackSpeed, 0); 
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        if (enemy.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!enemy.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f) + enemy.getxOffset();
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + enemy.getyOffset();
        batch.draw(currentFrame, drawX, drawY);
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        // TODO Auto-generated method stub
        super.drawEffects(batch, stateTime);
    }

    @Override
    public void exit() {
        // TODO Auto-generated method stub
        super.exit();
    }
    
}
