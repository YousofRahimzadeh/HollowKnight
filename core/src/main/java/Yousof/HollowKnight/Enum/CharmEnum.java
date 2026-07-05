package Yousof.HollowKnight.Enum;

public enum CharmEnum {
    SOUL_CATCHER("Soul Catcher", "Increases SOUL gained when striking enemies.", "animations/Atlas/Charms/soul_catcher.png"),
    DASHMASTER("Dashmaster", "Allows the bearer to dash more often.", "animations/Atlas/Charms/dashmaster.png"),
    UNBREAKABLE_STRENGTH("Unbreakable Strength", "Strengthens the bearer, increasing damage of regular attacks.", "animations/Atlas/Charms/strength.png"),
    QUICK_SLASH("Quick Slash", "Allows the bearer to slash much more rapidly.", "animations/Atlas/Charms/quick_slash.png"),
    QUICK_FOCUS("Quick Focus", "Increases the speed of focusing SOUL to heal.", "animations/Atlas/Charms/quick_focus.png"),
    HEAVY_BLOW("Heavy Blow", "Increases the knockback force of regular attacks.", "animations/Atlas/Charms/heavy_blow.png"),
    SHARP_SHADOW("Sharp Shadow", "Dash passes through enemies, dealing damage.", "animations/Atlas/Charms/sharp_shadow.png"),
    VOID_HEART("Void Heart", "Upgrades spells, increasing their damage by 50%.", "animations/Atlas/Charms/void_heart.png");

    private final String title;
    private final String description;
    private final String texturePath;

    CharmEnum(String title, String description, String texturePath) {
        this.title = title;
        this.description = description;
        this.texturePath = texturePath;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTexturePath() { return texturePath; }
}