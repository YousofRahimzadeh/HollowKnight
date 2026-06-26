package Yousof.HollowKnight.Model.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entitie {
    protected Body body;
    
    public Body getBody(){
        return body;
    }

    abstract public void update(float dt);
    abstract public void draw(Batch batch);
    abstract public void dispose();
}
