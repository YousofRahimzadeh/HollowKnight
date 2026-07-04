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