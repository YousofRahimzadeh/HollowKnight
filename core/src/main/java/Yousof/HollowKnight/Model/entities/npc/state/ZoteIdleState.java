package Yousof.HollowKnight.Model.entities.npc.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class ZoteIdleState extends ZoteState {

    private boolean drawE = false;
    private BitmapFont font;

    public ZoteIdleState() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiSkin.json"));
        this.font = skin.getFont("title");
    }

    @Override
    public void enter(Zote enemy) {
        super.enter(enemy);
        currentAnimation = AnimationManager.Zote.create("Idle", PlayMode.LOOP, 0.08f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        
        // اگر شوالیه نزدیک بود، دکمه E را فعال کن
        if (enemy.getSurroundSensor().knight != null) {
            drawE = true;
            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E)) {
                // فرض می‌کنیم متد تغییر وضعیت رو اینطوری داری:
                enemy.changeState(new ZoteDialogueState());
            }
        } else {
            drawE = false;
        }
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
        if(drawE){
            float eX = body.getPosition().x * Constants.PPM - 10f;
            float eY = drawY + currentFrame.getRegionHeight(); 
            
            font.draw(batch, "E", eX, eY);
        }

        drawEffects(batch, stateTime);
    }
}