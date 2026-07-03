package Yousof.HollowKnight.Utils.state;

import com.badlogic.gdx.math.MathUtils;

import Yousof.HollowKnight.Enum.Constants;

public class CameraDialogueState extends CameraState{

    @Override
    public void update(float delta) {
        super.update(delta);
        float targetX = (game.getKnight().getBody().getPosition().x + game.getZote().getBody().getPosition().x) * Constants.PPM / 2f ;
        float targetY = (game.getKnight().getBody().getPosition().y + game.getZote().getBody().getPosition().y) * Constants.PPM / 2f  + 150f;

        camera.position.x = MathUtils.lerp(camera.position.x, targetX, 0.1f);
        camera.position.y = MathUtils.lerp(camera.position.y, targetY, 0.1f);
    }

}
