package Yousof.HollowKnight.Utils.camera.state;

import com.badlogic.gdx.math.MathUtils;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Utils.camera.CameraSession;

public class CameraVibrationState extends CameraState {

    private float shakeDuration; 
    private float shakeTimer;    
    private float shakeIntensity;

    public CameraVibrationState(float duration, float intensity) {
        this.shakeDuration = duration;
        this.shakeIntensity = intensity;
        this.shakeTimer = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (game.getKnight() == null || game.getKnight().getBody() == null) return;

        float targetX = MathUtils.lerp(camera.position.x, game.getKnight().getBody().getPosition().x * Constants.PPM, 0.1f);
        float targetY = MathUtils.lerp(camera.position.y, game.getKnight().getBody().getPosition().y * Constants.PPM, 0.1f);
        
        float halfCamWidth = camera.viewportWidth / 2f;
        float halfCamHeight = camera.viewportHeight / 2f;
        
        float mapWidth = game.getMap().getProperties().get("width", Integer.class) * game.getMap().getProperties().get("tilewidth", Integer.class);
        float mapHeight = game.getMap().getProperties().get("height", Integer.class) * game.getMap().getProperties().get("tileheight", Integer.class);
        
        if (mapWidth > camera.viewportWidth) {
            targetX = MathUtils.clamp(targetX, halfCamWidth, mapWidth - halfCamWidth);
        } else {
            targetX = mapWidth / 2f;
        }
        
        if (mapHeight > camera.viewportHeight) {
            targetY = MathUtils.clamp(targetY, halfCamHeight, mapHeight - halfCamHeight);
        } else {
            targetY = mapHeight / 2f; 
        }
        

        if (shakeTimer < shakeDuration) {
            shakeTimer += delta;

            float offsetX = MathUtils.random(-1f, 1f) * shakeIntensity;
            float offsetY = MathUtils.random(-1f, 1f) * shakeIntensity;

            camera.position.x = targetX + offsetX;
            camera.position.y = targetY + offsetY;

            shakeIntensity = MathUtils.lerp(shakeIntensity, 0, delta * 5);

        } else {
            CameraSession.getInstance().changeState(new CameraKnightState());
        }
    }
}