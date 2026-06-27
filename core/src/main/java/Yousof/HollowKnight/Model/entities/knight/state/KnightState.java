package Yousof.HollowKnight.Model.entities.knight.state;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Model.entities.knight.Knight;

public abstract class KnightState {
    protected float stateTime;
    protected Knight knight;
    protected Body body;

    public void enter(Knight knight){
        stateTime = 0f;
        this.knight = knight;
        this.body = knight.getBody();
    }
    public void update(float delta){
        stateTime += delta;
    }
    public void draw(Batch batch){}
    public void exit(){}
    public void drawEffects(Batch batch, float stateTime){}
}
