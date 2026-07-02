package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Keys;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class KnightIdleState extends KnightState{

    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        animation = Animations.Knight.create("Idle", PlayMode.LOOP, 0.08f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        body.setLinearVelocity(0 , body.getLinearVelocity().y);
        if(Gdx.input.isKeyPressed(Keys.KNIGHTFOCUS.key)){
            knight.changeState(new KnightFocusState());
        }
        if(Gdx.input.isKeyPressed(Keys.KNIGHTRIGHT.key) || Gdx.input.isKeyPressed(Keys.KNIGHTLEFT.key)){
            knight.changeState(new KnightRunState());
        }
        if(Gdx.input.isKeyJustPressed(Keys.KNIGHTJUMP.key)){
            knight.setCanDoubleJump(true);
            knight.changeState(new KnightJumpState());
        }
        if(Gdx.input.isKeyPressed(Keys.KNIGHTDASH.key)){
            knight.changeState(new KnightDashState());
        }
        if(Gdx.input.isKeyPressed(Keys.KNIGHTATTACK.key)){
            knight.changeState(new KnightAttackState());
        }
        if(Gdx.input.isKeyPressed(Keys.KNIGHTVENGEFUL.key)){
            knight.changeState(new KnightVengefulSpiritState());
        }
        if(Gdx.input.isKeyPressed(Keys.KNIGHTSCREAM.key)){
            knight.changeState(new KnightHowlingWraiths());
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
