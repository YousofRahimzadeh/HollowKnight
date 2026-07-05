package Yousof.HollowKnight.Model.entities.knight.state;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.CharmEnum;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.audio.AudioStore;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.camera.state.CameraVibrationState;

public class KnightHowlingWraiths extends KnightState{

    private Animation<TextureRegion> effecAnimation;
    private int damage;
    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        if(knight.getCurrentSoul() < 33) {
            knight.changeState(new KnightIdleState());
            return;
        }
        AudioManager.getInstance().playSound(AudioStore.HollowKnightScream.path);
        String effectName = "SoulScream";
        damage = knight.getDamage();
        if(knight.getInventory().isEquipped(CharmEnum.VOID_HEART)){
            effectName = "ShadowScream";
            damage = (int)(damage * 1.5f);
        }
        knight.setCurrentSoul(knight.getCurrentSoul() - 33);
        animation = AnimationManager.Knight.create("Scream", PlayMode.NORMAL, 0.08f);
        effecAnimation = AnimationManager.KnightEffects.create(effectName, PlayMode.NORMAL, 0.08f);

        performAttack();
        CameraSession.getInstance().changeState(new CameraVibrationState(1f, 13f));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0 , body.getLinearVelocity().y);

        if(animation.isAnimationFinished(stateTime) && effecAnimation.isAnimationFinished(stateTime)){
            stateTime = 0;
            knight.changeState(new KnightIdleState());
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
        drawEffects(batch, stateTime);
        batch.draw(currentFrame, drawX, drawY);
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        TextureRegion currentFrame = effecAnimation.getKeyFrame(stateTime);
        if (!knight.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (knight.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 70;
        batch.draw(currentFrame, drawX, drawY);
    }

    private void performAttack(){
        ArrayList<Enemy> enemies = knight.getScreamSensros().wholeSensor;
        
        if(enemies != null && !enemies.isEmpty()){
            for(Enemy enemy : enemies){
                if(enemy != null){
                    enemy.takeDamage(knight.getBody() , damage , 14f);
                    enemy.takeDamage(knight.getBody() , damage , 14f);
                    enemy.takeDamage(knight.getBody() , damage , 14f);
                }
            }
        }
    }
    
}
