package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.sensors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class CrystalSeeSensors {
    public Knight knightRight = null;
    public Knight knightLeft = null;

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;

        float SensorWidth = hx * 4f;
        float SensorHeight = hy * 3f;

        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_KNIGHT;

        shape.setAsBox(SensorWidth, SensorHeight, new Vector2(hx + SensorWidth,-hy + SensorHeight), 0);
        body.createFixture(fdef).setUserData("CrystalGuardian_knightRight_is_around");
        
        shape.setAsBox(SensorWidth, SensorHeight, new Vector2(-hx - SensorWidth,-hy + SensorHeight), 0);
        body.createFixture(fdef).setUserData("CrystalGuardian_knightLeft_is_around");

        shape.dispose();
    }
}
