package Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.WingedSentry;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class WingedIdleState extends WingedSentryState{

    @Override
    public void enter(WingedSentry enemy) {
        super.enter(enemy);
        currentAnimation = AnimationManager.WingedSentry.create("Idle", PlayMode.LOOP, 0.08f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        Knight knight = enemy.getSensor().knight;
        
        if(knight != null){
            Vector2 enemyPos = body.getPosition();
            Vector2 knightPos = knight.getBody().getPosition(); 
            Vector2 direction = new Vector2(knightPos.x - enemyPos.x, knightPos.y - enemyPos.y);

            if (Math.abs(enemyPos.y - knightPos.y) < 0.5f) {
                enemy.changeState(new WingedAttackState());
                return;
            }
            
            direction = direction.nor(); 
            
            float speed = enemy.getSpeed();
            body.setLinearVelocity(direction.x * speed, direction.y * speed);
            if(direction.x > 0){
                enemy.setFacingRight(true);
            }else{
                enemy.setFacingRight(false);
            }
            
        } else {
            body.setLinearVelocity(0 , 0);
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
