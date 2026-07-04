package Yousof.HollowKnight.Model.entities.knight.state;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.camera.state.CameraVibrationState;

public class KnightKnockbackState extends KnightState{

    private KnightState stateWrapper;
    private float duration = 1.5f;
    private float knockDuration = 0.4f;
    private float timer = 0f;
    private Body attackerBody;
    private float strength;

    public KnightKnockbackState(Body enemyBody , Knight knight,KnightState lastState ,float strength) {
        this.attackerBody = enemyBody;
        this.knight = knight;
        this.strength = strength;
        this.stateWrapper = lastState;
        this.stateTime = lastState.stateTime;
        this.animation = lastState.animation;
        if(lastState instanceof KnightFocusState) changeState(new KnightIdleState());
        CameraSession.getInstance().changeState(new CameraVibrationState(1f, 10f));
    }

    @Override
    public void enter(Knight knight) {
        super.enter(knight);
        knight.setOnKnock(true);

        this.timer = 0f;


        if (body != null && attackerBody != null) {
            Vector2 enemyPos = body.getPosition();
            Vector2 attackerPos = attackerBody.getPosition();
            Vector2 direction = new Vector2(enemyPos.x - attackerPos.x, enemyPos.y - attackerPos.y).nor();


            body.setLinearVelocity(0, 0);

            body.applyLinearImpulse(
                direction.x * strength,
                strength * 0.3f,
                body.getWorldCenter().x,
                body.getWorldCenter().y,
                true
            );
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        timer += delta;
        if(timer < knockDuration) return;

        stateWrapper.update(delta);
        stateTime = stateWrapper.stateTime;

        if (timer >= duration) {
            knight.setOnKnock(false);
            knight.changeState(stateWrapper);
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        if (knight.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!knight.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 38;
        
        float oldColor = batch.getPackedColor(); 
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, Math.min( 1f , (stateTime) % 1f + 0.3f) );
        batch.draw(currentFrame, drawX, drawY);
        batch.setPackedColor(oldColor);
    }

    @Override
    public void exit() {

        if (body != null) {
            body.setLinearVelocity(0, 0);
        }
        super.exit();
    }

    public void changeState(KnightState newState){
        if(newState instanceof KnightFocusState && timer < duration) {
            changeState(new KnightIdleState());
            return;
        }
        if (stateWrapper != null) {
            stateWrapper.exit();
        }
        stateWrapper = newState;
        stateWrapper.enter(knight);

        animation = stateWrapper.animation;
        stateTime = stateWrapper.stateTime;
    }
}