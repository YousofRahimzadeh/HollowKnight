package Yousof.HollowKnight.Utils.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import Yousof.HollowKnight.Enum.GameMap;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class SaveManager {

    public static void saveGame(Knight knight, GameMap currentMap, boolean isBossDefeated, int slotNumber) {
        GameData data = new GameData();
        data.currentMasks = knight.getCurrentMasks();
        data.maxMasks = knight.getMaxMasks();
        data.currentSoul = knight.getCurrentSoul();
        data.knightX = knight.getBody().getPosition().x;
        data.knightY = knight.getBody().getPosition().y;
        data.isFalseKnightDefeated = isBossDefeated;
        data.currentMapName = currentMap;

        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonText = json.prettyPrint(data);

        FileHandle file = Gdx.files.local("saves/slot" + slotNumber + ".json");
        file.writeString(jsonText, false);
        
        Gdx.app.log("SAVE", "Game successfully saved to slot " + slotNumber);
    }

    public static GameData loadGame(int slotNumber) {
        FileHandle file = Gdx.files.local("saves/slot" + slotNumber + ".json");
        
        if (!file.exists()) {
            return null; 
        }

        Json json = new Json();
        GameData data = json.fromJson(GameData.class, file.readString());
        
        return data;
    }
}