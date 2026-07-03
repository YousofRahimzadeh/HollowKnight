package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Keys;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.CameraSession;
import Yousof.HollowKnight.Utils.state.CameraVibrationState;

public class KnightFocusState extends KnightState{

    private boolean theEnd = false;
    private boolean theStart = true;
    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        animation = Animations.Knight.create("Focus Start", PlayMode.LOOP, 0.08f);
        if(knight.getCurrentSoul() < 33){
            knight.changeState(new KnightIdleState());
            return;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        CameraSession.getInstance().changeState(new CameraVibrationState(0.5f, 4f));

        if(animation.isAnimationFinished(stateTime) && theStart){
            theStart = false;
            animation = Animations.Knight.create("Focus", PlayMode.LOOP, 0.08f);
            return;
        }
        
        if(stateTime >= knight.getFocusDuration() && !theEnd){
            animation = Animations.Knight.create("Focus Get", PlayMode.NORMAL, 0.08f);
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
        if(!Gdx.input.isKeyPressed(Keys.KNIGHTFOCUS.key)){
            knight.changeState(new KnightIdleState());
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
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        
        
    }
    
}
