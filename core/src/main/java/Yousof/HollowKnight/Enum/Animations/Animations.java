package Yousof.HollowKnight.Enum.Animations;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Yousof.HollowKnight.Model.Assets;

public enum Animations {
    Knight("Animations/Atlas/Knight/Knight.atlas"),
    Crawlid("Animations/Atlas/Enemies/Crawlid/Crawlid.atlas"),
    Mosscreep("Animations/Atlas/Enemies/Mosscreep/Mosscreep.atlas");

    
    private String atlasPath;
    
    Animations(String atlasPath) {
        this.atlasPath = atlasPath;
    }

    public static void loadAll(AssetManager assetManager) {
        for (Animations type : values()) {
            assetManager.load(type.atlasPath, TextureAtlas.class);
        }
        assetManager.finishLoading();
    }
    
    public Animation<TextureRegion> create(String regionName , Animation.PlayMode playMode, float frameDuration) {
        TextureAtlas atlas = Assets.manager.get(atlasPath);
        Animation<TextureRegion> anim = new Animation<>(frameDuration, atlas.findRegions(regionName));
        anim.setPlayMode(playMode);
        return anim;
    }
}