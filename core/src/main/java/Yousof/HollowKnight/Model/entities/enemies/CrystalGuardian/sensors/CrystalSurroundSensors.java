package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.sensors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;

public class CrystalSurroundSensors {
    public int rightWall = 0;
    public int leftWall = 0;
    public int rightCliff = 0;
    public int leftCliff = 0;
    public boolean rightCliffInitialized = false;
    public boolean leftCliffInitialized = false;

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;

        float sideSensorWidth = 3f / Constants.PPM;
        float sideSensorHeight = hy * 0.9f;
        float cliffSensorWidth = 3f / Constants.PPM;
        float cliffSensorHeight = 3f / Constants.PPM;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_GROUND;

        shape.setAsBox(sideSensorWidth, sideSensorHeight, new Vector2(hx + sideSensorWidth, 0), 0);
        body.createFixture(fdef).setUserData("HuskHornhead_sensor_wall_right");

        shape.setAsBox(sideSensorWidth, sideSensorHeight, new Vector2(-hx - sideSensorWidth, 0), 0);
        body.createFixture(fdef).setUserData("HuskHornhead_sensor_wall_left");

        shape.setAsBox(cliffSensorWidth, cliffSensorHeight, new Vector2(hx + cliffSensorWidth, -hy - cliffSensorHeight), 0);
        body.createFixture(fdef).setUserData("HuskHornhead_sensor_cliff_right");

        shape.setAsBox(cliffSensorWidth, cliffSensorHeight, new Vector2(-hx - cliffSensorWidth, -hy - cliffSensorHeight), 0);
        body.createFixture(fdef).setUserData("HuskHornhead_sensor_cliff_left");

        shape.dispose();

    }
}
