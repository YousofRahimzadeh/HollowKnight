package Yousof.HollowKnight.Model.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class WaveProjectile extends Projectile{
    private Animation<TextureRegion> animation;
    private int damage = 5;
    private float stateTime = 0;
    private boolean isFacingRight;
    private float speed;
    private static final float maxSpeed = 8f;

    public WaveProjectile(World world ,Vector2 position , boolean isFacingRight , int damage , Animation<TextureRegion> animation){
        this.isFacingRight = isFacingRight;
        this.damage = damage;
        this.animation = animation;
        createBody(world , position );
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

    public void createBody(World world , Vector2 position){
        float hx = 20 / Constants.PPM;
        float hy = 10 / Constants.PPM;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(position.x, position.y + 20f/Constants.PPM);
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
        body.createFixture(fdef).setUserData("Wave_main_body");
        shape.dispose();
    }

    @Override
    public int getDamage() {return damage;}
    public void setDamage(int damage) {this.damage = damage;}

}