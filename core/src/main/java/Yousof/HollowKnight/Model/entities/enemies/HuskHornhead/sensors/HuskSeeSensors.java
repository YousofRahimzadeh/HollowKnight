package Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.sensors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class HuskSeeSensors {
    public Knight knightRight = null;
    public Knight knightLeft = null;

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;

        float SensorWidth = hx * 3f;
        float SensorHeight = hy * 0.5f;

        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_KNIGHT;

        shape.setAsBox(SensorWidth, SensorHeight, new Vector2(hx + SensorWidth,0), 0);
        body.createFixture(fdef).setUserData("HuskHornhead_knightRight_is_around");
        
        shape.setAsBox(SensorWidth, SensorHeight, new Vector2(-hx - SensorWidth,0), 0);
        body.createFixture(fdef).setUserData("HuskHornhead_knightLeft_is_around");

        shape.dispose();
    }
}
