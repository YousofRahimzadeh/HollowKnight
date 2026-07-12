package Yousof.HollowKnight.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import Yousof.HollowKnight.Enum.AchievementTypes;

public class AchievementManager {

    private static final String PREFS_FILE = "hollowknight_achievements";

    /** All enemy type keys that must be killed for TRUE_HUNTER. */
    private static final String[] ALL_ENEMY_TYPES = {
        "Crawlid", "HuskHornhead", "WingedSentry", "CrystalGuardian", "FalseKnight"
    };

    // ── Core unlock ───────────────────────────────────────────────────────────

    /** Persistently unlocks an achievement (no-op if already unlocked). */
    public static void unlockAchievement(AchievementTypes achievement) {
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        if (!prefs.getBoolean("ACH_" + achievement.name(), false)) {
            prefs.putBoolean("ACH_" + achievement.name(), true);
            prefs.flush();
            Gdx.app.log("ACHIEVEMENT", "Unlocked: " + achievement.getTitle());
        }
    }

    // ── COMPLETION ────────────────────────────────────────────────────────────

    /** Call when the player finishes the game (VictoryModal opened). */
    public static void checkCompletion() {
        unlockAchievement(AchievementTypes.COMPLETION);
    }

    // ── SPEEDRUN ──────────────────────────────────────────────────────────────

    /** Call with the total run time in seconds when the game is completed. */
    public static void checkSpeedrun(float totalSeconds) {
        if (totalSeconds < 5 * 60f) {   // under 5 minutes
            unlockAchievement(AchievementTypes.SPEEDRUN);
        }
    }

    // ── TRUE_HUNTER ───────────────────────────────────────────────────────────

    /**
     * Call each time an enemy type is killed for the first time.
     * @param enemyType one of: "Crawlid", "HuskHornhead", "WingedSentry",
     *                          "CrystalGuardian", "FalseKnight"
     */
    public static void recordEnemyTypeKilled(String enemyType) {
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        prefs.putBoolean("KILLED_" + enemyType, true);
        prefs.flush();
        checkTrueHunter(prefs);
    }

    private static void checkTrueHunter(Preferences prefs) {
        for (String type : ALL_ENEMY_TYPES) {
            if (!prefs.getBoolean("KILLED_" + type, false)) return;
        }
        unlockAchievement(AchievementTypes.TRUE_HUNTER);
    }

    // ── GRUB_RESCUER ──────────────────────────────────────────────────────────

    /**
     * Call when the player rescues all grubs.
     * Requires grub tracking to be implemented in the map/level layer.
     */
    public static void checkGrubRescuer(int rescuedCount, int totalGrubs) {
        if (rescuedCount >= totalGrubs && totalGrubs > 0) {
            unlockAchievement(AchievementTypes.GRUB_RESCUER);
        }
    }

    public static void resetAllAchievements() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        prefs.clear();
        prefs.flush();
    }
}

