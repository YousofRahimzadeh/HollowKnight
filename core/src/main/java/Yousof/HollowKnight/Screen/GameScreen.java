package Yousof.HollowKnight.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.GameStore;

public class GameScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Box2DDebugRenderer worldDebuger;

    private GameStore game;
    
    public GameScreen(GameStore game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();

        GameController.loadGame(game);
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        mapRenderer = new OrthogonalTiledMapRenderer(game.getMap(), 1f);
        worldDebuger = new Box2DDebugRenderer();
        camera.position.set(game.getKnight().getPos().x * Constants.PPM, game.getKnight().getPos().y * Constants.PPM, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameController.updateGame(delta);
        
        camera.position.set(game.getKnight().getPos().x * Constants.PPM, game.getKnight().getPos().y * Constants.PPM, 0);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        GameController.drawGame(batch);
        batch.end();

        Matrix4 debugMatrix = camera.combined.cpy();
        debugMatrix.scale(Constants.PPM, Constants.PPM, 1f); 
        // worldDebuger.setDrawInactiveBodies(true);
        // worldDebuger.render(game.getWorld(), debugMatrix);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void dispose(){
        game.dispose();
        mapRenderer.dispose();
    }

}
