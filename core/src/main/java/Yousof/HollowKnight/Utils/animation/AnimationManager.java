package Yousof.HollowKnight.Utils.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum AnimationManager {
    Soul("Animations/Atlas/HUD/SoulAnim.atlas"),
    SoulContainer("Animations/Atlas/HUD/SoulContainer.atlas"),
    Mask("Animations/Atlas/HUD/KnightHp.atlas"),
    Zote("Animations/Atlas/NPC/Zote.atlas"),
    Knight("Animations/Atlas/Knight/Knight.atlas"),
    KnightProjectile("Animations/Atlas/Knight/KnightProjectile.atlas"),
    KnightEffects("Animations/Atlas/Knight/Effects.atlas"),
    Crawlid("Animations/Atlas/Enemies/Crawlid/Crawlid.atlas"),
    WingedSentry("Animations/Atlas/Enemies/WingedSentry/WingedSentry.atlas"),
    HuskHornhead("Animations/Atlas/Enemies/HuskHornhead/HuskHornhead.atlas"),
    CrystalGuardian("Animations/Atlas/Enemies/CrystalGuardian/CrystalGuardian.atlas"),
    Mosscreep("Animations/Atlas/Enemies/Mosscreep/Mosscreep.atlas"),
    FalseKnight("Animations/Atlas/Enemies/FalseKnight/FalseKnight.atlas");

    
    private String atlasPath;
    private static AssetManager assetManager;
    
    AnimationManager(String atlasPath) {
        this.atlasPath = atlasPath;
    }

    public static void loadAll(AssetManager assetsManager) {
        assetManager = assetsManager;
        for (AnimationManager type : values()) {
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