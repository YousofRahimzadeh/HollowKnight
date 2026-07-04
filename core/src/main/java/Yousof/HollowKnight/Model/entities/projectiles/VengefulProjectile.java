package Yousof.HollowKnight.Model.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class VengefulProjectile extends Projectile{
    private Animation<TextureRegion> animation;
    private int damage = 5;
    private float stateTime = 0;
    private boolean isFacingRight;
    private float speed;
    private static final float maxSpeed = 8f;

    public VengefulProjectile(World world ,Knight knight){
        isFacingRight = knight.isFacingRight();
        animation = AnimationManager.KnightProjectile.create("SoulBall", PlayMode.LOOP_PINGPONG, 0.08f);
        createBody(world , knight);
        speed = (isFacingRight) ? maxSpeed : -maxSpeed;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        stateTime += dt;
        body.setLinearVelocity(speed , 0);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        if (!isFacingRight && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (isFacingRight && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float drawX = body.getPosition().x * Constants.PPM - (currentFrame.getRegionWidth() / 2f);
        float drawY = body.getPosition().y * Constants.PPM - (currentFrame.getRegionHeight() / 2f);
        batch.draw(currentFrame, drawX, drawY);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void createBody(World world , Knight knight){
        float hx = 40 / Constants.PPM;
        float hy = 30 / Constants.PPM;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(knight.getBody().getPosition().x, knight.getBody().getPosition().y + 20f/Constants.PPM);
        body = world.createBody(bdef);
        body.setUserData(this);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy);
        fdef.shape = shape;
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_PROJECTILE;
        fdef.filter.maskBits = Constants.BIT_ENEMY | Constants.BIT_GROUND;
        body.createFixture(fdef).setUserData("Vengeful_main_body");
        shape.dispose();
    }

    public int getDamage() {return damage;}
    public void setDamage(int damage) {this.damage = damage;}

}