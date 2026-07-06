package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.KeysSettings;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class KnightIdleState extends KnightState{

    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        animation = AnimationManager.Knight.create("Idle", PlayMode.LOOP, 0.08f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(knight.isCanMove()){

            body.setLinearVelocity(0 , body.getLinearVelocity().y);
            if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTFOCUS.key)){
                knight.changeState(new KnightFocusState());
            }
            if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTRIGHT.key) || Gdx.input.isKeyPressed(KeysSettings.KNIGHTLEFT.key)){
                knight.changeState(new KnightRunState());
            }
            if(Gdx.input.isKeyJustPressed(KeysSettings.KNIGHTJUMP.key)){
                knight.setCanDoubleJump(true);
                knight.changeState(new KnightJumpState());
            }
            if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTDASH.key) && knight.isCanDash()){
                knight.changeState(new KnightDashState());
            }
            if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTATTACK.key)){
                knight.changeState(new KnightAttackState());
            }
            if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTVENGEFUL.key)){
                knight.changeState(new KnightVengefulSpiritState());
            }
            if(Gdx.input.isKeyPressed(KeysSettings.KNIGHTSCREAM.key)){
                knight.changeState(new KnightHowlingWraiths());
            }
            if(body.getLinearVelocity().y < 0 && knight.getSurroundSensors().downSensor <= 0){
                knight.changeState(new KnightFallState());
            }
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
