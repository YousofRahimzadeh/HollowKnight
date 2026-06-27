package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Keys;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class KnightJumpState extends KnightState{

    // private float timer;
    private Animation<TextureRegion> animation;

    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        animation = Animations.Knight.create("Airborne", PlayMode.NORMAL, 0.08f);
        body.setLinearVelocity(body.getLinearVelocity().x , 0f);
        body.applyLinearImpulse(new Vector2(0f , 5f), body.getPosition(), true);
        knight.setCanDoubleJump(true);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.key)){
            if(knight.getSurroundSensors().rightSensor > 0){
                knight.changeState(new KnightWallSideState());
                return;
            }
            knight.setFacingRight(true);
            body.setLinearVelocity(knight.getMaxSpeed() , body.getLinearVelocity().y);
        }

        if(Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.key)){
            if(knight.getSurroundSensors().leftSensor > 0){
                knight.changeState(new KnightWallSideState());
                return;
            }
            knight.setFacingRight(false);
            body.setLinearVelocity(-knight.getMaxSpeed() , body.getLinearVelocity().y);
        }

        if(Gdx.input.isKeyJustPressed(Keys.KNIGHTJUMP.key) && knight.isCanDoubleJump()){
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0f , 5f), body.getPosition(), true);
            knight.setCanDoubleJump(false);
            return;
        }

        if(body.getLinearVelocity().y < 0){
            knight.changeState(new KnightFallState());
            return;
        }

        if(Gdx.input.isKeyJustPressed(Keys.KNIGHTATTACK.key)){
            knight.changeState(new KnightAttackState());
            return;
        }

        if(Gdx.input.isKeyJustPressed(Keys.KNIGHTDASH.key)){
            knight.changeState(new KnightDashState());
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
