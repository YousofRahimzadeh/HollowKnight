package Yousof.HollowKnight.Model.HUD;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import Yousof.HollowKnight.Enum.Animations.Animations;

public class HeartIcon {
    public enum HeartState { FILLED, EMPTY, BREAKING, REFILLING }

    private HeartState state = HeartState.FILLED;
    
    // انیمیشن‌ها و فریم‌های ثابت
    private Animation<TextureRegion> FilledHealth;
    private Animation<TextureRegion> EmptyHealth;
    private Animation<TextureRegion> breakAnimation;
    private Animation<TextureRegion> refillAnimation;
    
    private float stateTime = 0f;

    public HeartIcon() {
        FilledHealth = Animations.Mask.create("FilledHealth", PlayMode.NORMAL, 0.1f);
        EmptyHealth = Animations.Mask.create("EmptyHealth", PlayMode.NORMAL, 0.1f);
        breakAnimation = Animations.Mask.create("BreakHealth", PlayMode.NORMAL, 0.1f);
        refillAnimation = Animations.Mask.create("HealthRefill", PlayMode.NORMAL, 0.1f);
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void changeState(HeartState newState) {

        if (this.state == newState) return;
        
        this.state = newState;
        this.stateTime = 0f;
    }

    public TextureRegion getCurrentFrame() {
        switch (state) {
            case BREAKING:
                return breakAnimation.getKeyFrame(stateTime);
            case REFILLING:
                return refillAnimation.getKeyFrame(stateTime);
            case EMPTY:
                return refillAnimation.getKeyFrame(stateTime);
            case FILLED:
                return refillAnimation.getKeyFrame(stateTime);
        }

    }
    
    public HeartState getState() {
        return state;
    }
}