package Yousof.HollowKnight.Enum.Animations;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum Animations {
    Knight("Animations/Atlas/Knight/Knight.atlas"),
    KnightEffects("Animations/Atlas/Knight/Effects.atlas"),
    Crawlid("Animations/Atlas/Enemies/Crawlid/Crawlid.atlas"),
    WingedSentry("Animations/Atlas/Enemies/WingedSentry/WingedSentry.atlas"),
    HuskHornhead("Animations/Atlas/Enemies/HuskHornhead/HuskHornhead.atlas"),
    CrystalGuardian("Animations/Atlas/Enemies/CrystalGuardian/CrystalGuardian.atlas"),
    Mosscreep("Animations/Atlas/Enemies/Mosscreep/Mosscreep.atlas");

    
    private String atlasPath;
    private static AssetManager assetManager;
    
    Animations(String atlasPath) {
        this.atlasPath = atlasPath;
    }

    public static void loadAll(AssetManager assetsManager) {
        assetManager = assetsManager;
        for (Animations type : values()) {
            assetManager.load(type.atlasPath, TextureAtlas.class);
        }
        assetManager.finishLoading();
    }
    
    public Animation<TextureRegion> create(String regionName , Animation.PlayMode playMode, float frameDuration) {
        TextureAtlas atlas = assetManager.get(atlasPath);
        Animation<TextureRegion> anim = new Animation<>(frameDuration, atlas.findRegions(regionName));
        anim.setPlayMode(playMode);
        return anim;
    }
}