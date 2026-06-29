package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.CrystalGuardian;
import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.sensors.CrystalSurroundSensors;

public class CrystalTurnState extends CrystalEnemyState{

    private CrystalSurroundSensors sensors;
    @Override
    public void enter(CrystalGuardian enemy) {
        super.enter(enemy);
        sensors = enemy.getSurroundSensors();
        currentAnimation = enemy.getAnimation().create("Turn", PlayMode.NORMAL, 0.08f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0f, body.getLinearVelocity().y);
        if(currentAnimation.isAnimationFinished(stateTime)){

            if(sensors.leftCliff == 0 && sensors.rightCliff == 0){
                return;
            }
            if(sensors.leftCliff == 0 || sensors.leftWall > 0){
                enemy.setFacingRight(true);
            }
            else if(sensors.rightCliff == 0 || sensors.rightWall > 0){
                enemy.setFacingRight(false);
            }
            enemy.changeState(new CrystalIdleState());
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
