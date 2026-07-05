package Yousof.HollowKnight.Model.entities.knight.state;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class KnightShadowDashState extends KnightState{

    private Animation<TextureRegion> dashAnim;
    private ArrayList<Enemy> hasAttacked;
    private float speedMulti = 1.4f;

    @Override
    public void enter(Knight knight) {  
        super.enter(knight);
        hasAttacked = new ArrayList<>();
        animation = AnimationManager.KnightShadowDash.create("Shadow Dash", PlayMode.NORMAL, 0.08f);
        dashAnim = AnimationManager.KnightEffects.create("Dash Effect", Animation.PlayMode.NORMAL, 0.06f);
        knight.startDashCooldown();
        knight.getBody().getFixtureList().forEach(f -> {
            if("Knight_main_body".equals(f.getUserData())){
                f.getFilterData().categoryBits = Constants.BIT_KNIGHT_DEAD;
            }
        });
        float speed = (knight.isFacingRight()) ? 12f : -12f;
        body.applyLinearImpulse(new Vector2(speed * speedMulti , 0), body.getWorldCenter() , true);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        
        body.setLinearVelocity(body.getLinearVelocity().x , 0f);
        
        if(animation.isAnimationFinished(stateTime)){
            body.setLinearVelocity(0f, 0f);
            if(knight.getSurroundSensors().downSensor > 0){
                knight.changeState(new KnightIdleState());
                return;
            }
            
            knight.setCanDoubleJump(true);
            knight.changeState(new KnightFallState());
        }
    }

    public void performAttack(Enemy enemy){
        if(hasAttacked.contains(enemy)) return;
        hasAttacked.add(enemy);
        enemy.takeDamage(body, 5, 12f);
    }

    @Override
    public void exit() {
        knight.getBody().getFixtureList().forEach(f -> {
            if("Knight_main_body".equals(f.getUserData())){
                f.getFilterData().categoryBits = Constants.BIT_KNIGHT;
            }
        });
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
        batch.draw(currentFrame, drawX, drawY);
        drawEffects(batch, stateTime);
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        TextureRegion effectFrame = null;
        float effectX = 0;
        float effectY = 0;

        float knightCenterX = body.getPosition().x * Constants.PPM;
        float knightCenterY = body.getPosition().y * Constants.PPM;
    
        effectFrame = dashAnim.getKeyFrame(stateTime, false);
        float dOffset = 250f;
    
        if (knight.isFacingRight()) {
            if (effectFrame.isFlipX()) effectFrame.flip(true, false); 
            effectX = knightCenterX - dOffset - (effectFrame.getRegionWidth() / 2f); 
        } else {
            if (!effectFrame.isFlipX()) effectFrame.flip(true, false);
            effectX = knightCenterX + dOffset - (effectFrame.getRegionWidth() / 2f);
        }
        effectY = knightCenterY - (effectFrame.getRegionHeight() / 2f); 

        if (effectFrame != null) {
            batch.draw(effectFrame, effectX, effectY);
        }
        
    }
    
}
