package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class FalseDeathState extends FalseKnightState {

    public enum LeapPhase { FALLING , LANDING , IDLE ,  HITING}
    public LeapPhase currentPhase;
    
    @Override
    public void enter(FalseKnightEnemy enemy) {
        super.enter(enemy);
        
        currentAnimation = AnimationManager.FalseKnight.create("DeathFall", PlayMode.NORMAL, enemy.frameDuration);
        currentPhase = LeapPhase.FALLING;
        enemy.cleanUpPhysicsOnDeath();

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // if(firstUpdate){
        //     reCreateBody();
        //     firstUpdate = false;
        // }
        body.setLinearVelocity(0 , body.getLinearVelocity().y);

        if (currentAnimation.isAnimationFinished(stateTime) && currentPhase == LeapPhase.FALLING) {
            currentAnimation = AnimationManager.FalseKnight.create("DeathLand", PlayMode.NORMAL, enemy.frameDuration);
            currentPhase = LeapPhase.LANDING;
            stateTime = 0f;
            return;
        } 

        if (currentAnimation.isAnimationFinished(stateTime) && currentPhase == LeapPhase.LANDING) {
            currentAnimation = AnimationManager.FalseKnight.create("Body", PlayMode.LOOP, enemy.frameDuration);
            currentPhase = LeapPhase.IDLE;
            stateTime = 0f;
            return;
        } 
    }


    public void reCreateBody(){
        super.reCreateBody();
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1f;
        fdef.friction = 0f;
        fdef.restitution = 0f;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_GROUND | Constants.BIT_KNIGHT;

        float bodyHx = (200f / Constants.PPM) / 2f;
        float bodyHy = (175f / Constants.PPM) / 2f;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bodyHx, bodyHy);
        fdef.shape = shape;
        fdef.isSensor = false;
        body.createFixture(fdef).setUserData("Enemy_main_body_null");

        float headHx = (100f / Constants.PPM) / 2f;
        float headHy = (100f / Constants.PPM) / 2f;
        float offsetX = (enemy.isFacingRight()) ? headHx + bodyHx : -headHx - bodyHx;
        float offsetY = headHx -bodyHx;
        shape.setAsBox(headHx, headHy , new Vector2(offsetX , offsetY) , 0f);
        fdef.isSensor = false;
        body.createFixture(fdef).setUserData("Enemy_main_body");

        enemy.getNearbySensors().createSensors(body, bodyHx, bodyHy);
        enemy.getFarSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getMiddleSensors().createSensors(body, bodyHx, bodyHy);
        enemy.getGroundSensors().createSensors(body, bodyHx, bodyHy);
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
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + 170f;
        batch.draw(currentFrame, drawX, drawY);
        drawEffects(batch, stateTime);
    }
}