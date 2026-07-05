package Yousof.HollowKnight.Utils.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import Yousof.HollowKnight.Enum.KeysSettings;

public class InteractiveKnightActor extends Actor {
    private final Preferences preferences;
    private Animation<TextureRegion> currentAnimation;
    private String currentAnimName = "";
    private float stateTime = 0f;
    
    private boolean faceRight = true;

    private static final float ATLAS_FRAME_WIDTH = 349f;
    private static final float ATLAS_FRAME_HEIGHT = 186f;

    public InteractiveKnightActor() {
        this.preferences = Gdx.app.getPreferences("hollowknight");
        changeAnimation("Idle", Animation.PlayMode.LOOP, 0.1f);
    }

    private void changeAnimation(String name, Animation.PlayMode mode, float duration) {
        if (currentAnimName.equals(name)) return; 
        currentAnimName = name;
        currentAnimation = AnimationManager.Knight.create(name, mode, duration);
        stateTime = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        int keyLeft = preferences.getInteger("KEY_KNIGHTLEFT", KeysSettings.KNIGHTLEFT.getKey());
        int keyRight = preferences.getInteger("KEY_KNIGHTRIGHT", KeysSettings.KNIGHTRIGHT.getKey());
        int keyAttack = preferences.getInteger("KEY_KNIGHTATTACK", KeysSettings.KNIGHTATTACK.getKey());
        int keyJump = preferences.getInteger("KEY_KNIGHTJUMP", KeysSettings.KNIGHTJUMP.getKey());
        int keyFocus = preferences.getInteger("KEY_KNIGHTFOCUS", KeysSettings.KNIGHTFOCUS.getKey());
        int keyDash = preferences.getInteger("KEY_KNIGHTDASH", KeysSettings.KNIGHTDASH.getKey());
        int keyScream = preferences.getInteger("KEY_KNIGHTSCREAM", KeysSettings.KNIGHTSCREAM.getKey());

        if (Gdx.input.isKeyPressed(keyRight)) {
            faceRight = true;
        } else if (Gdx.input.isKeyPressed(keyLeft)) {
            faceRight = false;
        }

        // بررسی اولویت انیمیشن‌ها
        if (Gdx.input.isKeyPressed(keyAttack)) {
            changeAnimation("Slash", Animation.PlayMode.LOOP, 0.06f);
        } else if (Gdx.input.isKeyPressed(keyDash)) {
            changeAnimation("Dash", Animation.PlayMode.LOOP, 0.08f);
        } else if (Gdx.input.isKeyPressed(keyFocus)) {
            changeAnimation("Focus", Animation.PlayMode.LOOP, 0.1f);
        } else if (Gdx.input.isKeyPressed(keyScream)) {
            changeAnimation("Scream", Animation.PlayMode.LOOP, 0.08f);
        } else if (Gdx.input.isKeyPressed(keyJump)) {
            changeAnimation("Airborne", Animation.PlayMode.LOOP, 0.1f);
        } else if (Gdx.input.isKeyPressed(keyLeft) || Gdx.input.isKeyPressed(keyRight)) {
            changeAnimation("Run", Animation.PlayMode.LOOP, 0.06f);
        } else {
            changeAnimation("Idle", Animation.PlayMode.LOOP, 0.1f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        if (currentFrame != null) {
            batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

            if ((faceRight && !currentFrame.isFlipX()) || (!faceRight && currentFrame.isFlipX())) {
                currentFrame.flip(true, false);
            }

            float cropWidth = ATLAS_FRAME_WIDTH * 0.35f;   
            float cropHeight = ATLAS_FRAME_HEIGHT * 0.65f; 
            
            float scaleX = getWidth() / cropWidth;
            float scaleY = getHeight() / cropHeight;
            float scale = Math.min(scaleX, scaleY); 

            float drawWidth = ATLAS_FRAME_WIDTH * scale;
            float drawHeight = ATLAS_FRAME_HEIGHT * scale;

            float drawX = getX() + (getWidth() - drawWidth) / 2f;
            float drawY = getY() + (getHeight() - drawHeight) / 2f;

            batch.draw(
                currentFrame, 
                drawX, 
                drawY, 
                getOriginX(), 
                getOriginY(), 
                drawWidth, 
                drawHeight, 
                getScaleX(), 
                getScaleY(), 
                getRotation()
            );
        }
    }
}