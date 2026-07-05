package Yousof.HollowKnight.Model.entities.knight.state;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.CharmEnum;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.KeysSettings;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.knight.sensors.KnightAttackSensors;
import Yousof.HollowKnight.Utils.animation.AnimationManager;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.audio.AudioStore;

public class KnightAttackState extends KnightState{

    private Direction currentDir;
    private enum Direction {
        HOR,
        DOWN,
        UP;
    }

    float frameRate = 0.08f;


    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        if(knight.getInventory().isEquipped(CharmEnum.QUICK_SLASH)){
            frameRate /= 2;
        }
        if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTUP.key)){
            currentDir = Direction.UP;
            animation = AnimationManager.Knight.create("UpSlash", PlayMode.NORMAL, frameRate);
        }else if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTDOWN.key) && knight.getSurroundSensors().downSensor == 0){
            currentDir = Direction.DOWN;
            animation = AnimationManager.Knight.create("DownSlash", PlayMode.NORMAL, frameRate);
        }else{
            currentDir = Direction.HOR;
            animation = AnimationManager.Knight.create("Slash", PlayMode.NORMAL, frameRate);
        }

        AudioManager.getInstance().playSound(AudioStore.HollowKnightSword.path);

        performAttack();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(animation.isAnimationFinished(stateTime)){
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
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
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
                effectFrame = AnimationManager.KnightEffects.create("SlashEffect", PlayMode.NORMAL, 0.06f).getKeyFrame(stateTime, false);

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
                effectFrame = AnimationManager.KnightEffects.create("UpSlashEffect", PlayMode.NORMAL, 0.06f).getKeyFrame(stateTime, false);

                float upOffset = 65f; 
                effectX = knightCenterX - (effectFrame.getRegionWidth() / 2f);
                effectY = knightCenterY + upOffset - (effectFrame.getRegionHeight() / 2f);

                if (effectFrame.isFlipX() != knight.isFacingRight()) {
                    effectFrame.flip(true, false);
                }
                break;

            case Direction.DOWN:
                effectFrame = AnimationManager.KnightEffects.create("DownSlashEffect", PlayMode.NORMAL, 0.06f).getKeyFrame(stateTime, false);

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
            knight.changeState(new KnightPogoJumpState());
            return;
        }
        float strength = 5f;
        int damage = knight.getDamage();
        if(knight.getInventory().isEquipped(CharmEnum.HEAVY_BLOW)){
            strength += 5f;
        }
        if(knight.getInventory().isEquipped(CharmEnum.UNBREAKABLE_STRENGTH)){
            damage += 5;
        }
        ArrayList<Enemy> targets = new ArrayList<>(enemies);
        if(targets != null && !targets.isEmpty()){
            for(Enemy enemy : targets){
                if(enemy != null){
                    enemy.takeDamage(knight.getBody() , damage , strength);
                    knight.addCurrentSoul();
                }
            }
        }
    }
}