package Yousof.HollowKnight.Utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Utils.state.CameraKnightState;
import Yousof.HollowKnight.Utils.state.CameraState;

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

    public static void setInstance(CameraSession session) {
        CameraSession.session = session;
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
