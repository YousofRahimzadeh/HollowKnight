package Yousof.HollowKnight.Screen.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Model.HUD.GameHUD;
import Yousof.HollowKnight.Screen.AbstractScreen;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.camera.CameraSession;

public class GameScreen extends AbstractScreen {

    private CameraSession camera;
    private ScreenViewport viewport;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Box2DDebugRenderer worldDebuger;

    private GameHUD hud;
    
    private GameState state;

    public GameScreen() {}

    @Override
    public void show() {
        super.show();
        
        camera = CameraSession.getInstance();
        viewport = new ScreenViewport(camera.getCamera());
        mapRenderer = new OrthogonalTiledMapRenderer(GameSession.getInstance().getMap(), 1f);
        worldDebuger = new Box2DDebugRenderer();
        hud = new GameHUD(GameSession.getInstance().getKnight());
        InputMultiplexer multiplexer = new InputMultiplexer();
        GameProcessor gameProcessor = new GameProcessor();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gameProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        AudioManager.getInstance().transitionToMusic(GameSession.getInstance().getMapName().getMusicPath(), true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameController.updateGame(delta);
        camera.update(delta);
        
        AnimatedTiledMapTile.updateAnimationBaseTime(); 
        mapRenderer.setView(CameraSession.getInstance().getCamera());
        int[] backLayers = {0 , 1 , 2 , 3};
        mapRenderer.render(backLayers);
        batch.setProjectionMatrix(camera.getCamera().combined);
        batch.begin();
        GameController.drawGame(batch , delta);
        batch.end();
        int[] foreLayers = {4 , 5 , 6};
        mapRenderer.render(foreLayers);

        hud.render(batch, delta);

        Matrix4 debugMatrix = camera.getCamera().combined.cpy();
        debugMatrix.scale(Constants.PPM, Constants.PPM, 1f); 
        // worldDebuger.setDrawInactiveBodies(true);
        // worldDebuger.render(GameSession.getInstance().getWorld(), debugMatrix);
        
        super.render(delta);
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

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

}
