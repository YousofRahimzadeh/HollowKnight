package Yousof.HollowKnight.Model.entities.knight.sensors;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;

public class KnightScreamSensros {
    public ArrayList<Enemy> wholeSensor;

    public KnightScreamSensros(){
        wholeSensor = new ArrayList<>();
    }

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;

        float SensorWidth = 100f / Constants.PPM;
        float SensorHeight = 100f / Constants.PPM;
        

        shape.setAsBox(SensorWidth, SensorHeight, new Vector2(0, SensorHeight - hy), 0);
        body.createFixture(fdef).setUserData("knight_scream_surround_sensor");

        shape.dispose();

    }
}
