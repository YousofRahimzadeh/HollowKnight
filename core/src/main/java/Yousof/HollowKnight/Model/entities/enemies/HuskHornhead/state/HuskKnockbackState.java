package Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.state;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.HuskHornheadEnemy;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.audio.AudioStore;

public class HuskKnockbackState extends HuskEnemyState{

    private float duration = 0.2f;
    private float timer = 0f;
    private Body attackerBody;
    private float strength;

    public HuskKnockbackState(Body attackerBody,HuskEnemyState lastState ,float strength) {
        this.attackerBody = attackerBody;
        this.strength = strength;
        this.stateTime = lastState.stateTime;
        this.currentAnimation = lastState.currentAnimation;
    }

    @Override
    public void enter(HuskHornheadEnemy enemy) {
        super.enter(enemy);
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

        AudioManager.getInstance().playSound(AudioStore.EnemyDamage.path);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        
        timer += delta;

        if (timer >= duration) {
            enemy.changeState(new HuskRunState());
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        if (enemy.isFacingRight() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!enemy.isFacingRight() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + enemy.getyOffset();
        
        float oldColor = batch.getPackedColor(); 
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.3f);
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
}