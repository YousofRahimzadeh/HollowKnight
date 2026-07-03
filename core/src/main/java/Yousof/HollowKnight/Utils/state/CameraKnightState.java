package Yousof.HollowKnight.Utils.state;

import com.badlogic.gdx.math.MathUtils;

import Yousof.HollowKnight.Enum.Constants;

public class CameraKnightState extends CameraState{

    @Override
    public void update(float delta) {
        super.update(delta);
        camera.position.x = MathUtils.lerp(camera.position.x, game.getKnight().getBody().getPosition().x * Constants.PPM, 0.1f);
        camera.position.y = MathUtils.lerp(camera.position.y, game.getKnight().getBody().getPosition().y * Constants.PPM, 0.1f);
    }

}
