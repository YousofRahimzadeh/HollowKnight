package Yousof.HollowKnight.Utils.camera.state;

import com.badlogic.gdx.math.MathUtils;
import Yousof.HollowKnight.Enum.Constants;

public class CameraKnightState extends CameraState {

    @Override
    public void update(float delta) {
        super.update(delta);
        
        float targetX = MathUtils.lerp(camera.position.x, game.getKnight().getBody().getPosition().x * Constants.PPM, 0.1f);
        float targetY = MathUtils.lerp(camera.position.y, game.getKnight().getBody().getPosition().y * Constants.PPM, 0.1f);
        
        float halfCamWidth = camera.viewportWidth / 2f;
        float halfCamHeight = camera.viewportHeight / 2f;
        
        float mapWidth = game.getMap().getProperties().get("width", Integer.class) * game.getMap().getProperties().get("tilewidth", Integer.class);
        float mapHeight = game.getMap().getProperties().get("height", Integer.class) * game.getMap().getProperties().get("tileheight", Integer.class);
        
        if (mapWidth > camera.viewportWidth) {
            camera.position.x = MathUtils.clamp(targetX, halfCamWidth, mapWidth - halfCamWidth);
        } else {
            camera.position.x = mapWidth / 2f;
        }
        
        if (mapHeight > camera.viewportHeight) {
            camera.position.y = MathUtils.clamp(targetY, halfCamHeight, mapHeight - halfCamHeight);
        } else {
            camera.position.y = mapHeight / 2f; 
        }
        
        camera.update();
    }
}