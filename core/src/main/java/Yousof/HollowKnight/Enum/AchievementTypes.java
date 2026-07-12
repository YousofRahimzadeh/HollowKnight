package Yousof.HollowKnight.Enum;

public enum AchievementTypes {
    COMPLETION("Completion", "Finish the main story of the game."),
    SPEEDRUN("Speedrun", "Beat the game in under 5 minutes."),
    TRUE_HUNTER("True Hunter", "Defeat at least one of every enemy type."),
    FALSE_KNIGHT("False Knight Slain", "Defeat the False Knight boss."),
    SEE_ZOTE("See Zote", "Encounter Zote the Mighty.");    
    private final String title;
    private final String description;

    AchievementTypes(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
}