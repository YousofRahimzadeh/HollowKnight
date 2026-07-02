package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.sensors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;

public class FalseGroundSensors {
    public int groundSensor = 0;

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;

        float HSensorWidth = hx;
        float HSensorHeight = 5f / Constants.PPM;

        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_KNIGHT | Constants.BIT_GROUND;

        shape.setAsBox(HSensorWidth, HSensorHeight, new Vector2(0f, -hy - HSensorHeight), 0);
        body.createFixture(fdef).setUserData("FalseKnight_ground_sensor");

        shape.dispose();
    }
}
