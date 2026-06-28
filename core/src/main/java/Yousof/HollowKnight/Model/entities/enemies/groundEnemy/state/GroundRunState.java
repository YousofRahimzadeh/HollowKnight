package Yousof.HollowKnight.Model.entities.enemies.groundEnemy.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.GroundEnemy;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.sensors.GroundSurroundSensors;

public class GroundRunState extends GroundEnemyState{

    private GroundSurroundSensors sensors;
    private float speed;
    @Override
    public void enter(GroundEnemy enemy) {
        super.enter(enemy);
        sensors = enemy.getSensors();
        currentAnimation = enemy.getAnimation().create("Walk", PlayMode.LOOP, 0.08f);
        speed = enemy.isFacingRight() ? enemy.getSpeed() : -enemy.getSpeed();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(speed , body.getLinearVelocity().y);
        if(sensors.leftCliff == 0 && sensors.rightCliff == 0){
            return;
        }
        if((sensors.leftCliff == 0 || sensors.leftWall > 0) && !enemy.isFacingRight()){
            enemy.changeState(new GroundTurnState());
            return;
        }
        if((sensors.rightCliff == 0 || sensors.rightWall > 0) && enemy.isFacingRight()){
            enemy.changeState(new GroundTurnState());
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
