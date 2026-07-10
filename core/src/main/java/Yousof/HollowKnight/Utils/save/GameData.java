package Yousof.HollowKnight.Utils.save;

import java.util.ArrayList;

import Yousof.HollowKnight.Enum.AchievementTypes;
import Yousof.HollowKnight.Enum.GameMap;

public class GameData {
    public int currentMasks;
    public int maxMasks;
    public int currentSoul;
    public float knightX;
    public float knightY;

    public int deathCount;
	public int enemiesDefeated;
	public float totalTimeElapsed;

    public GameMap currentMapName;
    public boolean isFalseKnightDefeated;
    public int currentPhase;
    
    public ArrayList<AchievementTypes> unlockedAchievements = new ArrayList<>();
}