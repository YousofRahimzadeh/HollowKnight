package Yousof.HollowKnight.Utils.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import Yousof.HollowKnight.Model.entities.knight.Knight;

public class SaveManager {

    // متد ذخیره بازی در فایل JSON
    public static void saveGame(Knight knight, boolean isBossDefeated, int slotNumber) {
        // ۱. پر کردن کلاس واسط با مقادیر واقعی بازی در آن لحظه
        GameData data = new GameData();
        data.currentMasks = knight.getCurrentMasks();
        data.maxMasks = knight.getMaxMasks();
        data.currentSoul = knight.getCurrentSoul();
        data.knightX = knight.getBody().getPosition().x;
        data.knightY = knight.getBody().getPosition().y;
        data.isFalseKnightDefeated = isBossDefeated;
        // پر کردن بقیه فیلدها...

        // ۲. تبدیل شیء جاوا به متن چیدمان شده‌ی JSON
        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonText = json.prettyPrint(data); // تبدیل به متن خوانا با فاصله مجازی

        // ۳. نوشتن مستقیم روی فایل (مثلاً توی پوشه فایل‌های کاربر)
        FileHandle file = Gdx.files.local("saves/slot" + slotNumber + ".json");
        file.writeString(jsonText, false);
        
        Gdx.app.log("SAVE", "Game successfully saved to slot " + slotNumber);
    }

    // متد لود کردن اطلاعات از فایل JSON
    public static GameData loadGame(int slotNumber) {
        FileHandle file = Gdx.files.local("saves/slot" + slotNumber + ".json");
        
        // اگر فایل ذخیره‌ای از قبل وجود نداشت
        if (!file.exists()) {
            return null; 
        }

        // تبدیل متن داخل فایل JSON به شیء کلاس GameData
        Json json = new Json();
        GameData data = json.fromJson(GameData.class, file.readString());
        
        return data;
    }
}