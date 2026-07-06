package Yousof.HollowKnight.Screen.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Yousof.HollowKnight.Enum.AudioStore;
import Yousof.HollowKnight.Screen.AbstractScreen;
import Yousof.HollowKnight.Utils.audio.AudioManager;

public class MainScreen extends AbstractScreen {

    private Table backgroundTable;

    @Override
    public void show() {
        super.show();

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        TextureRegionDrawable bg = new TextureRegionDrawable(new Texture(Gdx.files.internal("background.png")));
        backgroundTable.setBackground(bg);
        rootTable.addActor(backgroundTable);

        AudioManager.getInstance().transitionToMusic(AudioStore.HollowKnight.path, true);

        MainModal mainModal = new MainModal();
        mainModal.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        AudioManager.getInstance().update(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);    
    }
}