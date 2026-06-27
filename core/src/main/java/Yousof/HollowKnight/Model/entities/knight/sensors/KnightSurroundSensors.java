package Yousof.HollowKnight.Model.entities.knight.sensors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;

public class KnightSurroundSensors {
    public int rightSensor;
    public int leftSensor;
    public int downSensor;

    public KnightSurroundSensors(){
        rightSensor = 0;
        leftSensor = 0;
        downSensor = 0;
    }

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;
        
        float sideSensorWidth = 4f / Constants.PPM;
        float sideSensorHeight = hy * 0.8f;
        float groundSensorWidth = hx * 0.8f;
        float groundSensorHeight = 4f / Constants.PPM;
        

        shape.setAsBox(groundSensorWidth, groundSensorHeight, new Vector2(0, -hy -groundSensorHeight), 0);
        body.createFixture(fdef).setUserData("Knight_ground_sensor");

        shape.setAsBox(sideSensorWidth, sideSensorHeight, new Vector2(hx + sideSensorWidth, 0), 0);
        body.createFixture(fdef).setUserData("Knight_sensor_wall_right");

        shape.setAsBox(sideSensorWidth, sideSensorHeight, new Vector2(-hx - sideSensorWidth, 0), 0);
        body.createFixture(fdef).setUserData("Knight_sensor_wall_left");

        shape.dispose();

    }
}
