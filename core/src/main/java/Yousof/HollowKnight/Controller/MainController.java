package Yousof.HollowKnight.Controller;

import com.badlogic.gdx.assets.AssetManager;

import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class MainController {
    public static void loadAllAssests(AssetManager assetManager){
        AnimationManager.loadAll(assetManager);
    }
}
