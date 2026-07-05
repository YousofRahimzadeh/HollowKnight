package Yousof.HollowKnight.Enum;

import com.badlogic.gdx.Input.Keys;

public enum CheatKeys {
    BOSS_TELEPORT(Keys.B),
    NOCLIP(Keys.N),
    EMERGENCY_HEAL(Keys.H),
    REFILL_SOUL(Keys.R),
    GOD_MODE(Keys.G),
    INSTA_KILL(Keys.K);

    private final int triggerKey;

    CheatKeys(int triggerKey) {
        this.triggerKey = triggerKey;
    }

    public int getTriggerKey() {
        return triggerKey;
    }
}