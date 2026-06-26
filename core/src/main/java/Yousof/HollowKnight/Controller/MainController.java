package Yousof.HollowKnight.Controller;

import com.badlogic.gdx.assets.AssetManager;

import Yousof.HollowKnight.Enum.Animations.Animations;

public class MainController {
    public static void loadAllAssests(AssetManager assetManager){
        Animations.loadAll(assetManager);
    }
}
