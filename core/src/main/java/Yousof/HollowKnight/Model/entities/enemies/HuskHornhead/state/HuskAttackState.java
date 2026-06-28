package Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.HuskHornheadEnemy;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.sensors.HuskSurroundSensors;

public class HuskAttackState extends HuskEnemyState{

    private HuskSurroundSensors sensors;
    private float speed;
    private boolean startIsFinish;
    @Override
    public void enter(HuskHornheadEnemy enemy) {
        super.enter(enemy);
        startIsFinish = false;
        sensors = enemy.getSurroundSensors();
        currentAnimation = enemy.getAnimation().create("Attack Anticipate", PlayMode.NORMAL, 0.08f);
        body.setLinearVelocity(0f , body.getLinearVelocity().y);
        speed = enemy.isFacingRight() ? enemy.getSpeed() * 2f : -enemy.getSpeed() * 2f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(!currentAnimation.isAnimationFinished(stateTime) && !startIsFinish){
            startIsFinish = true;
            body.setLinearVelocity(0 , body.getLinearVelocity().y);
            currentAnimation = enemy.getAnimation().create("Attack Lunge", PlayMode.LOOP, 0.08f);
            return;
        }
        body.setLinearVelocity(speed , body.getLinearVelocity().y);

        if(sensors.leftCliff == 0 && sensors.rightCliff == 0){
            return;
        }
        if((sensors.leftCliff == 0 || sensors.leftWall > 0) && !enemy.isFacingRight()){
            enemy.changeState(new HuskTurnState());
            return;
        }
        if((sensors.rightCliff == 0 || sensors.rightWall > 0) && enemy.isFacingRight()){
            enemy.changeState(new HuskTurnState());
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
