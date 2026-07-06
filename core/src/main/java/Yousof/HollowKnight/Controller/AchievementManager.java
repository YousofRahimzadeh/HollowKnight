package Yousof.HollowKnight.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import Yousof.HollowKnight.Enum.AchievementTypes;

public class AchievementManager {
    
    public static void unlockAchievement(AchievementTypes achievement) {
        Preferences prefs = Gdx.app.getPreferences("hollowknight_achievements");
        
        if (!prefs.getBoolean("ACH_" + achievement.name(), false)) {
            prefs.putBoolean("ACH_" + achievement.name(), true);
            prefs.flush();
            Gdx.app.log("ACHIEVEMENT", "Unlocked: " + achievement.getTitle());
        }
    }
}
