package Yousof.HollowKnight.Model.entities.npc.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class ZoteAttackState extends ZoteState {

    private float speed = 60f /Constants.PPM;

    public ZoteAttackState() {}

    @Override
    public void enter(Zote enemy) {
        super.enter(enemy);
        currentAnimation = AnimationManager.Zote.create("Attack", PlayMode.LOOP, 0.08f);
        enemy.getBody().getFixtureList().forEach(f ->{
            if(f.getUserData().equals("Zote_main_body"))
                f.setSensor(false);
                f.getFilterData().maskBits |= Constants.BIT_KNIGHT;
        });
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        
        if (enemy.getSurroundSensor().knight != null) {
            if(enemy.getBody().getPosition().x > enemy.getSurroundSensor().knight.getBody().getPosition().x){
                enemy.setFacingRight(false);
                enemy.getBody().setLinearVelocity(-speed, enemy.getBody().getLinearVelocity().y);
            }else{
                enemy.setFacingRight(true);
                enemy.getBody().setLinearVelocity(speed, enemy.getBody().getLinearVelocity().y);
           }
        }else{
            enemy.changeState(new ZoteIdleState());
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
    }
}