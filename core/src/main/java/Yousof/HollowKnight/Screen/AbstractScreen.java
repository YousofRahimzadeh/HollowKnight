package Yousof.HollowKnight.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import Yousof.HollowKnight.Utils.audio.AudioManager;


public class AbstractScreen implements Screen {
    private Pixmap cursorPixmap;
    protected Skin skin;

    protected Stage stage;
    protected Stack mainStack;
    protected Table rootTable;
    protected Stack modalStack;

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

        mainStack = new Stack();
        rootTable = new Table();
        modalStack = new Stack();

        mainStack.add(rootTable);
        mainStack.add(modalStack);
        mainStack.setFillParent(true);

        stage.addActor(mainStack);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        AudioManager.getInstance().update(delta);
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
        cursorPixmap.dispose();
        stage.dispose();
        skin.dispose();
        batch.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stack getMainStack() {
        return mainStack;
    }

    public void setMainStack(Stack mainStack) {
        this.mainStack = mainStack;
    }

    public Table getRootTable() {
        return rootTable;
    }

    public void setRootTable(Table rootTable) {
        this.rootTable = rootTable;
    }

    public Stack getModalStack() {
        return modalStack;
    }

    public void setModalStack(Stack modalStack) {
        this.modalStack = modalStack;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
