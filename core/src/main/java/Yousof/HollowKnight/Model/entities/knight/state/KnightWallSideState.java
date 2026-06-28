package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Keys;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class KnightWallSideState extends KnightState{

    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        animation = Animations.Knight.create("Wall Slide", PlayMode.LOOP, 0.08f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(knight.getSurroundSensors().downSensor > 0){
            knight.changeState(new KnightIdleState());
            return;
        }

        if(Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.key) && knight.getSurroundSensors().rightSensor > 0){
            body.setLinearVelocity( 0f , body.getLinearVelocity().y * 0.8f);
            return;
        }

        if(Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.key) && knight.getSurroundSensors().leftSensor > 0){
            body.setLinearVelocity( 0f , body.getLinearVelocity().y * 0.8f);
            return;
        }

        if(Gdx.input.isKeyPressed(Keys.KNIGHTJUMP.key) && knight.getSurroundSensors().rightSensor > 0){
            body.applyLinearImpulse(new Vector2(-1f , 2f), body.getPosition(), true);
            knight.setFacingRight(false);
            return;
        }

        if(Gdx.input.isKeyPressed(Keys.KNIGHTJUMP.key) && knight.getSurroundSensors().leftSensor > 0){
            body.applyLinearImpulse(new Vector2(1f , 2f), body.getPosition(), true);
            knight.setFacingRight(true);
            return;
        }

        if(Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.key) && knight.getSurroundSensors().leftSensor > 0){
            body.applyLinearImpulse(new Vector2(1f , 0f), body.getPosition(), true);
            return;
        }

        if(Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.key) && knight.getSurroundSensors().rightSensor > 0){
            body.applyLinearImpulse(new Vector2(-1f , 0f), body.getPosition(), true);
            return;
        }

        knight.changeState(new KnightFallState());
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
