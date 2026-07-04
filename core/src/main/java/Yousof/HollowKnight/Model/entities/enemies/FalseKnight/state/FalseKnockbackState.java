package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.audio.AudioStore;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.camera.state.CameraVibrationState;

public class FalseKnockbackState extends FalseKnightState {

    private FalseKnightState stateWrapper;
    private float duration = 0.4f;       
    private float timer = 0f;
    private Body attackerBody;
    private float strength;

    public FalseKnockbackState(Body attackerBody, FalseKnightState lastState, float strength) {
        this.attackerBody = attackerBody;
        this.strength = strength;
        
        if (lastState instanceof FalseKnockbackState) {
            this.stateWrapper = ((FalseKnockbackState) lastState).stateWrapper;
        } else {
            this.stateWrapper = lastState;
        }
        
        this.stateTime = lastState.stateTime;
        this.currentAnimation = lastState.currentAnimation;
    }

    @Override
    public void enter(FalseKnightEnemy enemy) {
        super.enter(enemy);
        enemy.setOnKnock(true);
        this.timer = 0f;

        if (body != null && attackerBody != null) {
            Vector2 enemyPos = body.getPosition();
            Vector2 attackerPos = attackerBody.getPosition();
            
            float dirX = (enemyPos.x - attackerPos.x) > 0 ? 1f : -1f;

            body.setLinearVelocity(0, body.getLinearVelocity().y);

            body.applyLinearImpulse(
                new Vector2(dirX * strength, strength * 0.2f),
                body.getWorldCenter(),
                true
            );
        }
        CameraSession.getInstance().changeState(new CameraVibrationState(1f, 13f));
        AudioManager.getInstance().playSound(AudioStore.EnemyDamage.path);
    }

    @Override
    public void update(float delta) {
        timer += delta;
        stateWrapper.update(delta);

        if (timer >= duration) {
            enemy.setOnKnock(false); 
            
            if (stateWrapper != null) {
                enemy.changeState(stateWrapper);
            } else {
                enemy.changeState(new FalseIdleState());
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        if (currentAnimation == null) return;

        TextureRegion currentFrame = stateWrapper.currentAnimation.getKeyFrame(stateWrapper.stateTime);
        if (enemy.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!enemy.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 130f;
        
        float oldColor = batch.getPackedColor(); 
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.6f);
        batch.draw(currentFrame, drawX, drawY);
        batch.setPackedColor(oldColor);
    }

    @Override
    public void exit() {
        if (body != null) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
    }

    public void changeState(FalseKnightState newState) {
        if(stateWrapper != null){
            stateWrapper.exit();
        }
        stateWrapper = newState;
        stateWrapper.enter(enemy);
    }
}