package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.Animations.Animations;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.projectiles.ProjectileFactory;

public class KnightVengefulSpiritState extends KnightState{

    private Animation<TextureRegion> effecAnimation;
    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        if(knight.getCurrentSoul() < 33) {
            knight.changeState(new KnightIdleState());
            return;
        }
        
        knight.setCurrentSoul(knight.getCurrentSoul() - 33);
        animation = Animations.Knight.create("Fireball Cast", PlayMode.NORMAL, 0.08f);
        effecAnimation = Animations.KnightEffects.create("Blast", PlayMode.NORMAL, 0.08f);
        ProjectileFactory.createProjectile("VengefulProjectile");
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
        batch.draw(currentFrame, drawX, drawY);
        drawEffects(batch, stateTime);
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        TextureRegion currentFrame = effecAnimation.getKeyFrame(stateTime);
        if (!knight.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (knight.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float xOffset = (knight.isFacingRight()) ? 80f : -80f;
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f) + xOffset;
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 20;
        batch.draw(currentFrame, drawX, drawY);
    }
    
}