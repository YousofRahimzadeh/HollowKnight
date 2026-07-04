package Yousof.HollowKnight.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Model.HUD.GameHUD;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.audio.AudioStore;
import Yousof.HollowKnight.Utils.camera.CameraSession;

public class GameScreen extends AbstractScreen {
    private CameraSession camera;
    private ScreenViewport viewport;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Box2DDebugRenderer worldDebuger;

    private GameSession game;
    private GameHUD hud;
    
    public GameScreen(GameSession game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();

        GameController.loadGame(game);
        camera = CameraSession.getInstance();
        viewport = new ScreenViewport(camera.getCamera());
        mapRenderer = new OrthogonalTiledMapRenderer(game.getMap(), 1f);
        worldDebuger = new Box2DDebugRenderer();
        hud = new GameHUD(game.getKnight());
        AudioManager.getInstance().transitionToMusic(AudioStore.CityOfTears.path, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        AudioManager.getInstance().update(delta);

        GameController.updateGame(delta);
        
        camera.update(delta);
        mapRenderer.setView(camera.getCamera());
        mapRenderer.render();
        
        batch.setProjectionMatrix(camera.getCamera().combined);
        batch.begin();
        GameController.drawGame(batch , delta);
        batch.end();

        hud.render(batch, delta);

        Matrix4 debugMatrix = camera.getCamera().combined.cpy();
        debugMatrix.scale(Constants.PPM, Constants.PPM, 1f); 
        // worldDebuger.setDrawInactiveBodies(true);
        // worldDebuger.render(game.getWorld(), debugMatrix);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hud.resize(width, height);
    }

    @Override
    public void dispose(){
        GameController.disposeGame();
        hud.dispose();
        mapRenderer.dispose();
    }

}
