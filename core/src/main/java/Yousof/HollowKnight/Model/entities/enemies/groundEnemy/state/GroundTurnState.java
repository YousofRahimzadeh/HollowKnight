package Yousof.HollowKnight.Model.entities.enemies.groundEnemy.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.GroundEnemy;

public class GroundTurnState extends GroundEnemyState{

    @Override
    public void enter(GroundEnemy enemy) {
        super.enter(enemy);
        currentAnimation = enemy.getAnimation().create("Turn", PlayMode.NORMAL, 0.08f);
        enemy.setFacingRight(!enemy.isFacingRight());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0f, body.getLinearVelocity().y);
        if(currentAnimation.isAnimationFinished(stateTime)){
            enemy.changeState(new GroundRunState());
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

    }

    @Override
    public void exit() {
        super.exit();

    }
}
