package Yousof.HollowKnight.Utils.camera.state;

import com.badlogic.gdx.graphics.OrthographicCamera;

import Yousof.HollowKnight.Model.GameSession;

public abstract class CameraState {
    protected GameSession game;
    protected OrthographicCamera camera;
    protected float stateTime;

    public void enter(OrthographicCamera setCamera , GameSession setGame){
        this.game = setGame;
        this.camera = setCamera;
        this.stateTime = 0f;
    }
    public void update(float delta){
        stateTime += delta;
    }
    public void exit(){}
}
