package Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Manager.AchievementManager;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.HuskHornheadEnemy;

public class HuskDeathState extends HuskEnemyState{
    
    @Override
    public void enter(HuskHornheadEnemy enemy) {
        super.enter(enemy);
        GameSession.getInstance().incrementEnemiesDefeated();
        AchievementManager.recordEnemyTypeKilled("HuskHornhead");
        currentAnimation = enemy.getAnimation().create("Death Land", PlayMode.NORMAL, 0.08f);
        enemy.cleanUpPhysicsOnDeath();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0 , body.getLinearVelocity().y);
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
    }

    @Override
    public void exit() {
        super.exit();

    }
}
