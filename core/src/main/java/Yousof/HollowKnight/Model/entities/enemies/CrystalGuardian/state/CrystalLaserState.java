package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

import javax.sound.sampled.Line;

import org.w3c.dom.Text;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.CrystalGuardianEnemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class CrystalLaserState extends CrystalEnemyState{

    private Vector2 target;
    private float duration = 5f; 
    private boolean startIsFinish = false;
    private boolean middleIsFinish = false;

    private Animation<TextureRegion> circleLaserAnimation;
    private Animation<TextureRegion> lineLaserAnimation;

    @Override
    public void enter(CrystalGuardianEnemy enemy) {
        super.enter(enemy);
        currentAnimation = enemy.getAnimation().create("Shoot", PlayMode.NORMAL, 0.08f);
        circleLaserAnimation = enemy.getAnimation().create("CircleLaser", PlayMode.NORMAL, 0.08f);
        lineLaserAnimation = enemy.getAnimation().create("LineLaser", PlayMode.NORMAL, 0.08f);

        if(enemy.getSeeSensors().knightRight != null && enemy.isFacingRight()){
            target = enemy.getSeeSensors().knightRight.getBody().getPosition();
        }
        else if(enemy.getSeeSensors().knightLeft != null && !enemy.isFacingRight()){
            target = enemy.getSeeSensors().knightLeft.getBody().getPosition();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0 , body.getLinearVelocity().y);

        if(!startIsFinish){
            if(currentAnimation.isAnimationFinished(stateTime)){
                startIsFinish = true;
            }
            return;
        }

        if(!middleIsFinish){
            
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
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + enemy.getyOffset();
        batch.draw(currentFrame, drawX, drawY);
        drawEffects(batch, stateTime);

    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        super.drawEffects(batch, stateTime);
        if(startIsFinish){
            
        }
    }

    @Override
    public void exit() {
        super.exit();

    }
}
