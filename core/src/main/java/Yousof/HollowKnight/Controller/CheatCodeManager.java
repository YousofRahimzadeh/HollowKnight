package Yousof.HollowKnight.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import Yousof.HollowKnight.Enum.CheatKeys;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.knight.state.KnightIdleState;
import Yousof.HollowKnight.Model.entities.knight.state.KnightSpectatorModeState;

public class CheatCodeManager {

    public static void handleCheats(Knight knight) {
        
        if (!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
            return; 
        }

        // if (Gdx.input.isKeyJustPressed(CheatKeys.BOSS_TELEPORT.getTriggerKey())) {
        //     float bossArenaX = ; 
        //     float bossArenaY = 400f;
        //     knight.setPosition(bossArenaX, bossArenaY);
        //     Gdx.app.log("CHEAT", "Teleported to Boss Arena!");
        // }

        if (Gdx.input.isKeyJustPressed(CheatKeys.NOCLIP.getTriggerKey())) {
            if(knight.isOnSpectator()){
                knight.changeState(new KnightIdleState());
                knight.setOnSpectator(false);
            }else{
                knight.changeState(new KnightSpectatorModeState());
                knight.setOnSpectator(true);
            }
        }

        if (Gdx.input.isKeyJustPressed(CheatKeys.EMERGENCY_HEAL.getTriggerKey())) {
            if (knight.getCurrentMasks() < knight.getMaxMasks()) {
                knight.setCurrentMasks(knight.getCurrentMasks() + 1);
            }
        }

        if (Gdx.input.isKeyJustPressed(CheatKeys.REFILL_SOUL.getTriggerKey())) {
            knight.setCurrentSoul(knight.getMaxSoul());
        }

        if (Gdx.input.isKeyJustPressed(CheatKeys.GOD_MODE.getTriggerKey())) {
            knight.setOnGodMode(!knight.isOnGodMode());
        }

        if (Gdx.input.isKeyJustPressed(CheatKeys.INSTA_KILL.getTriggerKey())) {
            for(Enemy enemy :GameSession.getInstance().getEnemies())
                enemy.takeDamage(enemy.getBody() , 9999 , 0);
            
        }
    }

}