package Yousof.HollowKnight.Model.HUD;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import Yousof.HollowKnight.Utils.animation.AnimationManager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HeartIcon {
    public enum HeartState { FILLED, EMPTY, BREAKING, REFILLING }

    private HeartState state = HeartState.FILLED;
    
    private Animation<TextureRegion> FilledHealth;
    private Animation<TextureRegion> EmptyHealth;
    private Animation<TextureRegion> breakAnimation;
    private Animation<TextureRegion> refillAnimation;
    
    private float stateTime = 0f;

    public HeartIcon() {
        FilledHealth = AnimationManager.Mask.create("FilledHealth", PlayMode.NORMAL, 0.1f);
        EmptyHealth = AnimationManager.Mask.create("EmptyHealth", PlayMode.NORMAL, 0.1f);
        breakAnimation = AnimationManager.Mask.create("BreakHealth", PlayMode.NORMAL, 0.1f);
        refillAnimation = AnimationManager.Mask.create("HealthRefill", PlayMode.NORMAL, 0.1f);
    }

    public void update(float delta) {
        stateTime += delta;

        if (state == HeartState.BREAKING && breakAnimation.isAnimationFinished(stateTime)) {
            changeState(HeartState.EMPTY);
        }
        
        if (state == HeartState.REFILLING && refillAnimation.isAnimationFinished(stateTime)) {
            changeState(HeartState.FILLED);
        }
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
                return EmptyHealth.getKeyFrame(stateTime);
            case FILLED:
                return FilledHealth.getKeyFrame(stateTime);
        }
        return null;
    }
    
    public HeartState getState() {
        return state;
    }
}