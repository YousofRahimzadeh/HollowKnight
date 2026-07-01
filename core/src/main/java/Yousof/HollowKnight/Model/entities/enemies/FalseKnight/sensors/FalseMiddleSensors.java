package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.sensors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class FalseMiddleSensors {
    public Knight knight = null;

    public void createSensors(Body body , float hx , float hy){
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        fdef.friction = 0f;
        fdef.isSensor = true;
        fdef.shape = shape;

        float HSensorWidth = (750f + 550f) / Constants.PPM / 2f;
        float HSensorHeight = hy + 100f / Constants.PPM;

        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_ENEMY;
        fdef.filter.maskBits = Constants.BIT_KNIGHT;

        shape.setAsBox(HSensorWidth, HSensorHeight, new Vector2(0f, -hy + HSensorHeight), 0);
        body.createFixture(fdef).setUserData("FalseKnight_knight_middle");

        shape.dispose();
    }
}

