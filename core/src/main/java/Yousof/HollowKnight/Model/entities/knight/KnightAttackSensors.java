package Yousof.HollowKnight.Model.entities.knight;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;

public class KnightAttackSensors {
    public ArrayList<Enemy> rightSensor;
    public ArrayList<Enemy> leftSensor;
    public ArrayList<Enemy> downSensor;
    public ArrayList<Enemy> upSensor;

    public KnightAttackSensors(){
        rightSensor = new ArrayList<>();
        leftSensor = new ArrayList<>();
        downSensor = new ArrayList<>();
        upSensor = new ArrayList<>();
    }

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;

        float rightLeftSensorWidth = 40f / Constants.PPM;
        float rightLeftSensorHeight = hy;
        float upDownSensorWidth = 3 * hx;
        float upDownSensorHeight = 40f / Constants.PPM;
        

        shape.setAsBox(upDownSensorWidth, upDownSensorHeight, new Vector2(0, -hy -upDownSensorHeight), 0);
        body.createFixture(fdef).setUserData("knight_attack_down_sensor");

        shape.setAsBox(rightLeftSensorWidth, rightLeftSensorHeight, new Vector2(hx + rightLeftSensorWidth, 0), 0);
        body.createFixture(fdef).setUserData("knight_attack_right_sensor");

        shape.setAsBox(rightLeftSensorWidth, rightLeftSensorHeight, new Vector2(-hx - rightLeftSensorWidth, 0), 0);
        body.createFixture(fdef).setUserData("knight_attack_left_sensor");

        shape.setAsBox(upDownSensorWidth, upDownSensorHeight, new Vector2(0, hy + upDownSensorHeight), 0);
        body.createFixture(fdef).setUserData("knight_attack_up_sensor");

        shape.dispose();

    }
}
