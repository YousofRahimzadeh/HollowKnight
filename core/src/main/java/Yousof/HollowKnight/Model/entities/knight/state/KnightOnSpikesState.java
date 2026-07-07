package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.camera.state.CameraVibrationState;

public class KnightOnSpikesState extends KnightState {

    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        animation = AnimationManager.Knight.create("Death", PlayMode.NORMAL, 0.08f);
        
        CameraSession.getInstance().changeState(new CameraVibrationState(1f, 16f));
        
        if (body != null) {
            body.setLinearVelocity(0, 0);
        }
    }   

    @Override
    public void update(float delta) {
        super.update(delta);
        
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        
        if (animation.isAnimationFinished(stateTime)) {
            
            body.setTransform(knight.getLasPos(), 0);
            
            body.setLinearVelocity(0, 0); 
            
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
        
        batch.draw(currentFrame, drawX, drawY);
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
    }
}