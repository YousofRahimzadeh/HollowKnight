package Yousof.HollowKnight.Utils.save;

import java.util.ArrayList;

public class GameData {
    // آمار شوالیه
    public int currentMasks;
    public int maxMasks;
    public int currentSoul;
    public float knightX;
    public float knightY;
    
    // وضعیت جهان بازی
    public String currentMapName;
    public boolean isFalseKnightDefeated;
    public int currentPhase;
    
    // دستاوردها
    public ArrayList<String> unlockedAchievements = new ArrayList<>();
}