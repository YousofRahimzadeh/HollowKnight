package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.CrystalGuardianEnemy;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.sensors.HuskSeeSensors;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.sensors.HuskSurroundSensors;

public class CrystalEnragedState extends CrystalEnemyState{

    private float duration = 5f;
    private HuskSurroundSensors sensors;
    private HuskSeeSensors seeSensors;
    private float speed;
    @Override
    public void enter(CrystalGuardianEnemy enemy) {
        super.enter(enemy);
        sensors = enemy.getSurroundSensors();
        seeSensors = enemy.getSeeSensors();
        currentAnimation = enemy.getAnimation().create("Run", PlayMode.LOOP, 0.08f);
        speed = enemy.isFacingRight() ? enemy.getSpeed() : -enemy.getSpeed();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(speed , body.getLinearVelocity().y);

        if(stateTime >= duration) enemy.changeState(new CrystalIdleState());

        if(seeSensors.knightRight != null && enemy.isFacingRight() || 
            seeSensors.knightLeft != null && !enemy.isFacingRight()) 
            enemy.changeState(new CrystalEnragedState());

        if(sensors.leftCliff == 0 && sensors.rightCliff == 0){
            return;
        }
        if((sensors.leftCliff == 0 || sensors.leftWall > 0) && !enemy.isFacingRight()){
            enemy.changeState(new CrystalTurnState());
            return;
        }
        if((sensors.rightCliff == 0 || sensors.rightWall > 0) && enemy.isFacingRight()){
            enemy.changeState(new CrystalTurnState());
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
