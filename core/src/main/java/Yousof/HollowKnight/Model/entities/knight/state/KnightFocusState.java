package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.CharmEnum;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.KeysSettings;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.audio.AudioStore;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.camera.state.CameraVibrationState;

public class KnightFocusState extends KnightState{

    private boolean theEnd = false;
    private boolean theStart = true;
    private float factor = 1f;
    private float frameRate = 0.08f;
    private float duration = 1.5f;
    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        if(knight.getCurrentSoul() < 33){
            knight.changeState(new KnightIdleState());
            return;
        }
        if(knight.getInventory().isEquipped(CharmEnum.QUICK_FOCUS)) factor = 2f;
        duration /= factor;
        animation = AnimationManager.Knight.create("Focus Start", PlayMode.LOOP, frameRate);
        AudioManager.getInstance().playSound(AudioStore.HollowKnightFocus.path);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        CameraSession.getInstance().changeState(new CameraVibrationState(0.5f / factor, 4f));

        if(animation.isAnimationFinished(stateTime) && theStart){
            theStart = false;
            animation = AnimationManager.Knight.create("Focus", PlayMode.LOOP, frameRate);
            return;
        }
        
        if(stateTime >= duration && !theEnd){
            animation = AnimationManager.Knight.create("Focus Get", PlayMode.NORMAL, frameRate);
            stateTime = 0;
            theEnd = true;
            knight.addMaskRemoveSoul();
            return;
        }

        if(animation.isAnimationFinished(stateTime) && theEnd){
            knight.changeState(new KnightIdleState());
            return;
        }
        
        body.setLinearVelocity(0 , body.getLinearVelocity().y);
        if(!Gdx.input.isKeyPressed(KeysSettings.KNIGHTFOCUS.key)){
            knight.changeState(new KnightIdleState());
            return;
        }
    }

    @Override
    public void exit() {
        AudioManager.getInstance().stopSound(AudioStore.HollowKnightFocus.path);
        
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
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        
        
    }
    
}
