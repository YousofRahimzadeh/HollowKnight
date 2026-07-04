package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.KeysSettings;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.audio.AudioStore;

public class KnightFallState extends KnightState{

    private boolean isLanding = false;
    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        animation = AnimationManager.Knight.create("Fall", PlayMode.LOOP, 0.08f);
        // AudioManager.getInstance().playSound(AudioStore.HollowKnightFly.path);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(isLanding && animation.isAnimationFinished(stateTime)){
            knight.changeState(new KnightIdleState());
            return;
        }

        if(!isLanding && knight.getSurroundSensors().downSensor > 0){
            isLanding = true;
            stateTime = 0f;
            animation = AnimationManager.Knight.create("Landing", PlayMode.NORMAL, 0.08f);
            body.setLinearVelocity(0f , 0f);
            return;
        }

        if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTRIGHT.key)){
            if(knight.getSurroundSensors().rightSensor > 0){
                knight.changeState(new KnightWallSideState());
                return;
            }else if (knight.getSurroundSensors().downSensor > 0){
                knight.changeState(new KnightRunState());
                return;
            }
            knight.setFacingRight(true);
            body.setLinearVelocity(knight.getMaxSpeed() , body.getLinearVelocity().y);
        }

        if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTLEFT.key)){
            if(knight.getSurroundSensors().leftSensor > 0){
                knight.changeState(new KnightWallSideState());
                return;
            }else if (knight.getSurroundSensors().downSensor > 0){
                knight.changeState(new KnightRunState());
                return;
            }
            knight.setFacingRight(false);
            body.setLinearVelocity(-knight.getMaxSpeed() , body.getLinearVelocity().y);
        }

        if(Gdx.input.isKeyJustPressed(KeysSettings.KNIGHTATTACK.key)){
            knight.changeState(new KnightAttackState());
            return;
        }

        if(Gdx.input.isKeyJustPressed(KeysSettings.KNIGHTJUMP.key) && knight.isCanDoubleJump()){
            knight.setCanDoubleJump(false);
            knight.changeState(new KnightJumpState());
            return;
        }

        if(Gdx.input.isKeyJustPressed(KeysSettings.KNIGHTDASH.key)){
            knight.changeState(new KnightDashState());
            return;
        }

        if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTVENGEFUL.key)){
            knight.changeState(new KnightVengefulSpiritState());
        }

        
    }

    @Override
    public void exit() {
        // AudioManager.getInstance().stopSound(AudioStore.HollowKnightFly.path);
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
