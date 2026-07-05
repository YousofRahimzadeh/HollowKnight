package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.KeysSettings;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class KnightSpectatorModeState extends KnightState {

    private float originalGravityScale;

    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        
        animation = AnimationManager.Knight.create("Idle", PlayMode.LOOP, 0.08f);

        this.originalGravityScale = body.getGravityScale();
        body.setGravityScale(0);
        body.setLinearVelocity(0, 0);

        Filter filter = new Filter();
        filter.categoryBits = Constants.BIT_KNIGHT_DEAD;
        filter.maskBits = 0;
        
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
        
        Gdx.app.log("STATE", "Knight entered GOD / NOCLIP Mode!");
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        float flySpeed = 8f; 
        float moveX = 0;
        float moveY = 0;

        if (Gdx.input.isKeyPressed(KeysSettings.KNIGHTRIGHT.key)) {
            moveX = flySpeed;
            knight.setFacingRight(true);
        }
        if (Gdx.input.isKeyPressed(KeysSettings.KNIGHTLEFT.key)) {
            moveX = -flySpeed;
            knight.setFacingRight(false);
        }
        if (Gdx.input.isKeyPressed(KeysSettings.KNIGHTJUMP.key) || Gdx.input.isKeyPressed(Keys.UP)) {
            moveY = flySpeed;
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            moveY = -flySpeed;
        }

        body.setLinearVelocity(moveX, moveY);
    }

    @Override
    public void exit() {
        body.setGravityScale(originalGravityScale);
        
        Filter filter = new Filter();
        filter.categoryBits = Constants.BIT_KNIGHT;
        filter.maskBits = Constants.BIT_ENEMY | Constants.BIT_GROUND | Constants.BIT_NPC; 
        
        for (Fixture fixture : body.getFixtureList()) {
            if(fixture.isSensor())
                fixture.setFilterData(new Filter());
            else
                fixture.setFilterData(filter);
        }
        Gdx.app.log("STATE", "Knight exited GOD / NOCLIP Mode!");
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
        
        batch.setColor(1, 1, 1, 0.7f); 
        batch.draw(currentFrame, drawX, drawY);
        batch.setColor(1, 1, 1, 1f); 
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
    }
}