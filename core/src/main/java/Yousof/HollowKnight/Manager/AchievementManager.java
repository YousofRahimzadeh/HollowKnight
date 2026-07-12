package Yousof.HollowKnight.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import Yousof.HollowKnight.Enum.AchievementTypes;

public class AchievementManager {

    private static final String PREFS_FILE = "hollowknight_achievements";

    private static final String[] ALL_ENEMY_TYPES = {
        "Crawlid", "HuskHornhead", "WingedSentry", "CrystalGuardian", "FalseKnight"
    };

    // ── Core unlock ───────────────────────────────────────────────────────────
    public static void unlockAchievement(AchievementTypes achievement) {
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        if (!prefs.getBoolean("ACH_" + achievement.name(), false)) {
            prefs.putBoolean("ACH_" + achievement.name(), true);
            prefs.flush();
            Gdx.app.log("ACHIEVEMENT", "Unlocked: " + achievement.getTitle());
        }
    }

    // ── COMPLETION ────────────────────────────────────────────────────────────
    public static void checkCompletion() {
        unlockAchievement(AchievementTypes.COMPLETION);
    }

    // ── SPEEDRUN ──────────────────────────────────────────────────────────────
    public static void checkSpeedrun(float totalSeconds) {
        if (totalSeconds < 5 * 60f) {   // under 5 minutes
            unlockAchievement(AchievementTypes.SPEEDRUN);
        }
    }

    // ── TRUE_HUNTER ───────────────────────────────────────────────────────────
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

    public static void resetAllAchievements() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_FILE);
        prefs.clear();
        prefs.flush();
    }
}

