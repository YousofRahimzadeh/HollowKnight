package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.CrystalGuardian;
import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.sensors.CrystalSeeSensors;
import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.sensors.CrystalSurroundSensors;

public class CrystalEnragedState extends CrystalEnemyState{

    private float duration = 5f;
    private CrystalSurroundSensors sensors;
    private CrystalSeeSensors seeSensors;
    private float speed;
    @Override
    public void enter(CrystalGuardian enemy) {
        super.enter(enemy);
        sensors = enemy.getSurroundSensors();
        seeSensors = enemy.getSeeSensors();
        currentAnimation = enemy.getAnimation().create("Run", PlayMode.LOOP, 0.08f);
        speed = enemy.getSpeed();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(stateTime >= duration) enemy.changeState(new CrystalIdleState());

        if(seeSensors.knightRight != null){
            if(!enemy.isFacingRight()) enemy.changeState(new CrystalTurnState());
            body.setLinearVelocity(speed , body.getLinearVelocity().y);
        }else if(seeSensors.knightLeft != null){
            if(enemy.isFacingRight()) enemy.changeState(new CrystalTurnState());
            body.setLinearVelocity(-speed , body.getLinearVelocity().y);
        } else {
            enemy.changeState(new CrystalIdleState());
        }

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
