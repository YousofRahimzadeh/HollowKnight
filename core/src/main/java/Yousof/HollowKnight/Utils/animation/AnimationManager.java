package Yousof.HollowKnight.Utils.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum AnimationManager {
    Soul("animations/Atlas/HUD/SoulAnim.atlas"),
    SoulContainer("animations/Atlas/HUD/SoulContainer.atlas"),
    Mask("animations/Atlas/HUD/KnightHp.atlas"),
    Zote("animations/Atlas/NPC/Zote.atlas"),
    Knight("animations/Atlas/Knight/Knight.atlas"),
    KnightProjectile("animations/Atlas/Knight/KnightProjectile.atlas"),
    KnightShadowDash("animations/Atlas/Knight/shadowDash.atlas"),
    KnightEffects("animations/Atlas/Knight/Effects.atlas"),
    Crawlid("animations/Atlas/Enemies/Crawlid/Crawlid.atlas"),
    WingedSentry("animations/Atlas/Enemies/WingedSentry/WingedSentry.atlas"),
    HuskHornhead("animations/Atlas/Enemies/HuskHornhead/HuskHornhead.atlas"),
    CrystalGuardian("animations/Atlas/Enemies/CrystalGuardian/CrystalGuardian.atlas"),
    Mosscreep("animations/Atlas/Enemies/Mosscreep/Mosscreep.atlas"),
    Tiktik("animations/Atlas/Enemies/Tiktik/Tiktik.atlas"),
    CrystalCrawler("animations/Atlas/Enemies/CrystalCrawler/CrystalCrawler.atlas"),
    FalseKnight("animations/Atlas/Enemies/FalseKnight/FalseKnight.atlas");

    
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