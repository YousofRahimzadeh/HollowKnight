package Yousof.HollowKnight.Model.entities.knight.state;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Yousof.HollowKnight.Enum.CharmEnum;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class KnightPogoJumpState extends KnightState{

    private Animation<TextureRegion> effectAnimation;
    private float frameRate = 0.08f;
    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        if(knight.getInventory().isEquipped(CharmEnum.QUICK_SLASH)){
            frameRate /= 2;
        }
        animation = AnimationManager.Knight.create("DownSlash", PlayMode.NORMAL, frameRate);
        effectAnimation = AnimationManager.KnightEffects.create("DownSlashEffect", PlayMode.NORMAL, frameRate);
    
        boolean spikeDetected = knight.getAttackSensors().spikesOnDown > 0;
        boolean enemyDetected = !knight.getAttackSensors().downSensor.isEmpty();

        if (spikeDetected || enemyDetected) {
            knight.setCanDoubleJump(true);
            knight.setCanDash(true);
            knight.getBody().setLinearVelocity(knight.getBody().getLinearVelocity().x , 0f);
            knight.getBody().applyLinearImpulse(new Vector2(0f , 5f), knight.getBody().getWorldCenter(), true);

            if (enemyDetected) performAttack();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(animation.isAnimationFinished(stateTime)){
            knight.changeState(new KnightFallState());
            return;
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
        TextureRegion effectFrame = effectAnimation.getKeyFrame(stateTime);
        float downOffset = 45f;
        float effectX = body.getPosition().x * Constants.PPM - (effectFrame.getRegionWidth() / 2f);
        float effectY = body.getPosition().y * Constants.PPM - downOffset - (effectFrame.getRegionHeight() / 2f);
        if (effectFrame.isFlipX() != knight.isFacingRight()) {
            effectFrame.flip(true, false);
        }
        batch.draw(effectFrame, effectX, effectY);
    }

    private void performAttack() {
        ArrayList<Enemy> enemies = knight.getAttackSensors().downSensor;
        
        if (enemies == null || enemies.isEmpty()) {
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
        
        java.util.Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            try {
                Enemy enemy = iterator.next();
                if(knight.getInventory().isEquipped(CharmEnum.HEAVY_BLOW)) strength += 5f;
                if (enemy != null && enemy.getBody() != null) {

                    enemy.takeDamage(knight.getBody(), damage , strength);
                    knight.addCurrentSoul();

                } else {
                    iterator.remove();
                }

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
}
