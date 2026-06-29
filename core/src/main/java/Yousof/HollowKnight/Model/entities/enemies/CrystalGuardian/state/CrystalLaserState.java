package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.CrystalGuardian;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class CrystalLaserState extends CrystalEnemyState {

    private float duration = 2f;
    private float laserTimer = 0f;
    private boolean startIsFinish = false;
    private boolean middleIsFinish = false;
    
    private Animation<TextureRegion> circleLaserAnimation;
    private Animation<TextureRegion> lineLaserAnimation;
    
    private Vector2 target;
    private Vector2 startPoint;
    private Vector2 hitPoint;
    private Sprite laserSprite;
    private float laserThickness = 0.9f;
    private float originX;
    private float originY;

    @Override
    public void enter(CrystalGuardian enemy) {
        super.enter(enemy);
        currentAnimation = enemy.getAnimation().create("Shoot", PlayMode.NORMAL, 0.08f);
        circleLaserAnimation = enemy.getAnimation().create("CircleLaser", PlayMode.LOOP_PINGPONG, 0.08f);
        lineLaserAnimation = enemy.getAnimation().create("LineLaser", PlayMode.LOOP_PINGPONG, 0.05f);

        startPoint = new Vector2();
        hitPoint = new Vector2();
        laserTimer = 0f;
        startIsFinish = false;
        middleIsFinish = false;

        if(enemy.getSeeSensors().knightRight != null && enemy.isFacingRight()){
            target = new Vector2(enemy.getSeeSensors().knightRight.getBody().getPosition());
        }
        else if(enemy.getSeeSensors().knightLeft != null && !enemy.isFacingRight()){
            target = new Vector2(enemy.getSeeSensors().knightLeft.getBody().getPosition());
        } else {
            float dir = enemy.isFacingRight() ? 5f : -5f;
            target = new Vector2(body.getPosition().x + dir, body.getPosition().y);
        }

        laserSprite = new Sprite(lineLaserAnimation.getKeyFrame(0));
        originX = (enemy.isFacingRight()) ? 20f : -20f;
        originY = 20f;
        laserSprite.setOrigin(0, (laserThickness * Constants.PPM) / 2f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        body.setLinearVelocity(0, body.getLinearVelocity().y);

        if (!startIsFinish) {
            if (currentAnimation.isAnimationFinished(stateTime)) {
                startIsFinish = true;
                stateTime = 0;
            }
            if(enemy.getSeeSensors().knightRight != null && enemy.isFacingRight()){
                target = new Vector2(enemy.getSeeSensors().knightRight.getBody().getPosition());
            }
            else if(enemy.getSeeSensors().knightLeft != null && !enemy.isFacingRight()){
                target = new Vector2(enemy.getSeeSensors().knightLeft.getBody().getPosition());
            } else {
                float dir = enemy.isFacingRight() ? 5f : -5f;
                target = new Vector2(body.getPosition().x + dir, body.getPosition().y);
            }
            return;
        }

        if (!middleIsFinish) {
            laserTimer += delta;
            
            float offsetXInMeters = originX * 3f / Constants.PPM;
            float offsetYInMeters = originY * 1f / Constants.PPM;
            startPoint.set(body.getPosition().x + offsetXInMeters, body.getPosition().y + offsetYInMeters);

            Vector2 direction = new Vector2(target).sub(startPoint).nor();
            
            float maxRange = 30f; 
            Vector2 endPoint = new Vector2(startPoint).add(direction.scl(maxRange));
            
            hitPoint.set(endPoint);

            enemy.getBody().getWorld().rayCast(new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    Object userData = fixture.getUserData();
                    Body targetBody = fixture.getBody();
                    
                    if (userData != null) {
                        if (userData.equals("grounds") || userData.equals("wall")) {
                            hitPoint.set(point);
                            return fraction;
                        }
                        if (userData.equals("Knight_main_body")) {
                            hitPoint.set(point);
                            ((Knight)targetBody.getUserData()).takeDamage(enemy);
                            return fraction;
                        }
                    }
                    return -1;
                }
            }, startPoint, endPoint);

            updateLaserSprite();

            if (laserTimer >= duration) {
                middleIsFinish = true;
                enemy.changeState(new CrystalEnragedState());
            }
        }
    }

    private void updateLaserSprite() {
        float distanceInPixels = startPoint.dst(hitPoint) * Constants.PPM;
        laserSprite.setSize(distanceInPixels, laserThickness * Constants.PPM);
        
        laserSprite.setPosition(startPoint.x * Constants.PPM, startPoint.y * Constants.PPM - (laserSprite.getHeight() / 2f));
        
        Vector2 diff = new Vector2(hitPoint).sub(startPoint);
        float angle = diff.angleDeg();
        laserSprite.setRotation(angle);
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
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f) + enemy.getyOffset();
        batch.draw(currentFrame, drawX, drawY);
        
        drawEffects(batch, stateTime);
    }

    @Override
    public void drawEffects(Batch batch, float stateTime) {
        super.drawEffects(batch, stateTime);
        
        if (startIsFinish && !middleIsFinish) {
            laserSprite.setRegion(lineLaserAnimation.getKeyFrame(laserTimer));
            laserSprite.draw(batch);
            TextureRegion circleFrame = circleLaserAnimation.getKeyFrame(stateTime);
            if(!enemy.isFacingRight() && !circleFrame.isFlipX()) circleFrame.flip(true, false);
            float circleX = body.getPosition().x * Constants.PPM - (circleFrame.getRegionWidth() / 2f);
            float circleY = body.getPosition().y * Constants.PPM - (circleFrame.getRegionHeight() / 2f) + originY;
            batch.draw(circleFrame, circleX, circleY);
        }
    }
}