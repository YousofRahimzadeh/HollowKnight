package Yousof.HollowKnight.Model.entities.npc.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class ZoteDeathState extends ZoteState {


    public ZoteDeathState() {}

    @Override
    public void enter(Zote enemy) {
        super.enter(enemy);
        currentAnimation = AnimationManager.Zote.create("Fall", PlayMode.NORMAL, 0.08f);
        enemy.getBody().getFixtureList().forEach(f ->{
            if(f.getUserData().equals("Zote_main_body"))
                f.setSensor(false);
                f.getFilterData().maskBits = Constants.BIT_DEAD_ENEMY;
        });
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    @Override
    public void draw(Batch batch) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        if (enemy.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!enemy.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 35f;
        batch.draw(currentFrame, drawX, drawY);
    }
}