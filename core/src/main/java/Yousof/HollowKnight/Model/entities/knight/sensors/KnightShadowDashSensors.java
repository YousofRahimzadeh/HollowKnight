package Yousof.HollowKnight.Model.entities.knight.sensors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;

public class KnightShadowDashSensors {

    public KnightShadowDashSensors(){}

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;
        fdef.filter.categoryBits = Constants.BIT_KNIGHT;
        fdef.filter.maskBits = Constants.BIT_ENEMY;

        float SensorWidth = hx * 1.4f;
        float SensorHeight = hy * 0.7f;
        

        shape.setAsBox(SensorWidth, SensorHeight, new Vector2(0, SensorHeight - hy), 0);
        body.createFixture(fdef).setUserData("knight_shadow_dash_sensor");

        shape.dispose();

    }
}
