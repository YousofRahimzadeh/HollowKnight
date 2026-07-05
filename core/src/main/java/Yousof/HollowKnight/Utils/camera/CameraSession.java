package Yousof.HollowKnight.Utils.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;

import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Utils.camera.state.CameraKnightState;
import Yousof.HollowKnight.Utils.camera.state.CameraState;

public class CameraSession {
    private static CameraSession session;
    
    private OrthographicCamera camera;
    private GameSession game;

    private CameraState currentState;

    private CameraSession(){
        camera = new OrthographicCamera();
        game = GameSession.getInstance();
        changeState(new CameraKnightState());
    }
    public static CameraSession getInstance(){
        if(session == null){
            session = new CameraSession();
        }
        return session;
    }

    public static CameraSession createInstance(){
        session = new CameraSession();
        return session;
    }

    public void update(float delta){
        currentState.update(delta);
        camera.update();
    }

    public void changeState(CameraState newState){
        if(currentState != null){
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(camera, game);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
