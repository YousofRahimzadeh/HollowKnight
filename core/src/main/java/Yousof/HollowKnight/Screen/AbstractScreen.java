package Yousof.HollowKnight.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;


public class AbstractScreen implements Screen {
    public static Music music;
    private Pixmap cursorPixmap;
    protected Skin skin;
    protected Stage stage;
    protected Table rootTable;
    protected SpriteBatch batch;
    protected Camera stageCamera;
    protected ScalingViewport stageViewport;

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("ui/uiSkin.json"));

        cursorPixmap = new Pixmap(Gdx.files.internal("cursor.png"));
        Cursor customCursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);

        Gdx.graphics.setCursor(customCursor);
        batch = new SpriteBatch();
        stageCamera = new OrthographicCamera();
        stageViewport = new ScalingViewport(Scaling.fit, 1920, 1080, stageCamera);
        stage = new Stage(stageViewport);
        Image background = new Image(new Texture("background.png"));
        background.setFillParent(true);
        stage.addActor(background);
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        stage.addActor(rootTable);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0 , 0 ,0 ,1));
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width , height , true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        music.dispose();
        cursorPixmap.dispose();
        stage.dispose();
        skin.dispose();
        batch.dispose();
    }
}
