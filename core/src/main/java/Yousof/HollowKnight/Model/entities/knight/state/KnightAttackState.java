package Yousof.HollowKnight.Model.entities.knight.state;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Keys;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightAttackSensors;

public class KnightAttackState extends KnightState{
    private Animation<TextureRegion> currentAnimation;

    private Direction currentDir;
    private enum Direction {
        HOR,
        DOWN,
        UP;
    }


    @Override
    public void enter(Knight knight) {  
        super.enter(knight);

        if(Gdx.input.isKeyPressed(Keys.KNIGHTUP.key)){
            currentDir = Direction.UP;
            currentAnimation = Animations.Knight.create("UpSlash", PlayMode.NORMAL, 0.08f);
        }else if(Gdx.input.isKeyPressed(Keys.KNIGHTDOWN.key) && knight.getSurroundSensors().downSensor == 0){
            currentDir = Direction.DOWN;
            currentAnimation = Animations.Knight.create("DownSlash", PlayMode.NORMAL, 0.08f);
        }else{
            currentDir = Direction.HOR;
            currentAnimation = Animations.Knight.create("Slash", PlayMode.NORMAL, 0.08f);
        }
        performAttack();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(currentAnimation.isAnimationFinished(stateTime)){
            if(knight.getSurroundSensors().downSensor > 0){
                knight.changeState(new KnightIdleState());
                return;
            }
            knight.changeState(new KnightFallState());
        }
    }

    @Override
    public void exit() {
        
    }

    @Override
    public void draw(Batch batch) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        if (knight.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!knight.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 38;
        batch.draw(currentFrame, drawX, drawY);
        drawEffects(batch, stateTime);
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        TextureRegion effectFrame = null;
        float effectX = 0;
        float effectY = 0;

        float knightCenterX = body.getPosition().x * Constants.PPM;
        float knightCenterY = body.getPosition().y * Constants.PPM;

        switch (currentDir) {
            case Direction.HOR:
                effectFrame = Animations.KnightEffects.create("SlashEffect", PlayMode.NORMAL, 0.06f).getKeyFrame(stateTime, false);

                float attackOffset = 45f; 

                if (knight.isFacingRight()) {
                    if (!effectFrame.isFlipX()) effectFrame.flip(true, false);
                    effectX = knightCenterX + attackOffset - (effectFrame.getRegionWidth() / 2f);
                } else {
                    if (effectFrame.isFlipX()) effectFrame.flip(true, false);
                    effectX = knightCenterX - attackOffset - (effectFrame.getRegionWidth() / 2f);
                }
                effectY = knightCenterY - (effectFrame.getRegionHeight() / 2f) + 10f; 
                break;

            case Direction.UP:
                effectFrame = Animations.KnightEffects.create("UpSlashEffect", PlayMode.NORMAL, 0.06f).getKeyFrame(stateTime, false);

                float upOffset = 65f; 
                effectX = knightCenterX - (effectFrame.getRegionWidth() / 2f);
                effectY = knightCenterY + upOffset - (effectFrame.getRegionHeight() / 2f);

                if (effectFrame.isFlipX() != knight.isFacingRight()) {
                    effectFrame.flip(true, false);
                }
                break;

            case Direction.DOWN:
                effectFrame = Animations.KnightEffects.create("DownSlashEffect", PlayMode.NORMAL, 0.06f).getKeyFrame(stateTime, false);

                float downOffset = 45f;
                effectX = knightCenterX - (effectFrame.getRegionWidth() / 2f);
                effectY = knightCenterY - downOffset - (effectFrame.getRegionHeight() / 2f);

                if (effectFrame.isFlipX() != knight.isFacingRight()) {
                    effectFrame.flip(true, false);
                }
                break;
            default:
                break;
            }

        if (effectFrame != null) {
            batch.draw(effectFrame, effectX, effectY);
        }
        
    }

    private void performAttack(){
        ArrayList<Enemy> enemies = null;
        KnightAttackSensors attackSensors = knight.getAttackSensors();
        if(currentDir == Direction.HOR){
            if(knight.isFacingRight())
                enemies = attackSensors.rightSensor;
            else
                enemies = attackSensors.leftSensor;
        } else if(currentDir == Direction.UP){
            enemies = attackSensors.upSensor;
        } else if(currentDir == Direction.DOWN){
            enemies = attackSensors.downSensor;
            knight.setCanDoubleJump(true);
        }
        
        if(enemies != null && !enemies.isEmpty()){
            for(Enemy enemy : enemies){
                if(enemy != null){
                    enemy.takeDamage(knight.getDamage());
                }
            }
        }
    }
}