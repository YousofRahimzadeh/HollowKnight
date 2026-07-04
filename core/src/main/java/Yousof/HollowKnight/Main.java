package Yousof.HollowKnight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;

import Yousof.HollowKnight.Controller.MainController;
import Yousof.HollowKnight.Model.Assets;
import Yousof.HollowKnight.Screen.MainScreen;
import Yousof.HollowKnight.Utils.audio.AudioManager;
import Yousof.HollowKnight.Utils.audio.AudioStore;

public class Main extends Game {
    public static Main main;

    public static Main getInstance(){
        return main;
    }
    @Override
    public void create() {
        main = this;
        MainController.loadAllAssests(Assets.manager);
        AudioManager.getInstance().transitionToMusic(AudioStore.HollowKnight.path, true);
        setScreen(new MainScreen());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        super.render();
    }

    @Override
    public void dispose(){
        Assets.manager.dispose();
        super.dispose();
    }
}
