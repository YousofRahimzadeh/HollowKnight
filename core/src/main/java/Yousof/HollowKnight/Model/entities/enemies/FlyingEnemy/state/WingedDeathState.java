package Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Manager.AchievementManager;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.WingedSentry;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class WingedDeathState extends WingedSentryState{

    @Override
    public void enter(WingedSentry enemy) {
        super.enter(enemy);
        GameSession.getInstance().incrementEnemiesDefeated();
        AchievementManager.recordEnemyTypeKilled("WingedSentry");
        currentAnimation = AnimationManager.WingedSentry.create("Death", PlayMode.NORMAL, 0.08f);
        enemy.getBody().setGravityScale(1f);
        enemy.cleanUpPhysicsOnDeath();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        enemy.getBody().setLinearVelocity(0f , enemy.getBody().getLinearVelocity().y);
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
        super.drawEffects(batch, stateTime);
    }

    @Override
    public void exit() {
        super.exit();
    }
    
}
