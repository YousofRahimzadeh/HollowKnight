package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class KnightDashState extends KnightState{


    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        animation = Animations.Knight.create("Dash", PlayMode.NORMAL, 0.08f);
        float speed = (knight.isFacingRight()) ? 12f : -12f;
        body.applyLinearImpulse(new Vector2(speed , 0), body.getWorldCenter() , true);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        
        body.setLinearVelocity(body.getLinearVelocity().x , 0f);
        
        if(animation.isAnimationFinished(stateTime)){
            body.setLinearVelocity(0f, 0f);
            if(knight.getSurroundSensors().downSensor > 0){
                knight.changeState(new KnightIdleState());
                return;
            }
            
            knight.setCanDoubleJump(true);
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

        Animation<TextureRegion> dashAnim = Animations.KnightEffects.create("Dash Effect", Animation.PlayMode.NORMAL, 0.06f);
    
        effectFrame = dashAnim.getKeyFrame(stateTime, false);
        float dOffset = 250f;
    
        if (knight.isFacingRight()) {
            if (effectFrame.isFlipX()) effectFrame.flip(true, false); 
            effectX = knightCenterX - dOffset - (effectFrame.getRegionWidth() / 2f); 
        } else {
            if (!effectFrame.isFlipX()) effectFrame.flip(true, false);
            effectX = knightCenterX + dOffset - (effectFrame.getRegionWidth() / 2f);
        }
        effectY = knightCenterY - (effectFrame.getRegionHeight() / 2f); 

        if (effectFrame != null) {
            batch.draw(effectFrame, effectX, effectY);
        }
        
    }
    
}
