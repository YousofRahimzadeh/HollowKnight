package Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.sensors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class WingedSentrySensor {
    public Knight knight = null;

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;

        float SensorWidth = hx * 2 ;
        float SensorHeight = hy * 2 ;

        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_KNIGHT;

        shape.setAsBox(SensorWidth, SensorHeight, body.getPosition(), 0);
        body.createFixture(fdef).setUserData("WingedSentry_knight_is_around");

        shape.dispose();
    }
}
