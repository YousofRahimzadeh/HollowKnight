package Yousof.HollowKnight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import Yousof.HollowKnight.Controller.MainController;
import Yousof.HollowKnight.Enum.Settings;
import Yousof.HollowKnight.Model.Assets;
import Yousof.HollowKnight.Screen.MainScreen;

public class Main extends Game {
    public static Main main;
    private Texture whiteOverlay;

    public static Main getInstance(){
        return main;
    }
    @Override
    public void create() {
        main = this;
        MainController.loadAllAssests(Assets.manager);
        setScreen(new MainScreen());
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whiteOverlay = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        super.render();
         // فرضا این مقدار بین 0.0 (تاریک) تا 2.0 (خیلی روشن) توسط اسلایدر تنظیم می‌شود
        // عدد 1.0 یعنی حالت نرمال و طبیعی بازی
        float brightness = Settings.brightness; 
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        if (brightness < 1.0f) {
            // حالت تاریک‌کننده: رنگ مشکی با شفافیتی مابین ۰ تا ۱
            batch.setColor(0, 0, 0, 1.0f - brightness); 
            batch.draw(whiteOverlay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } 
        else if (brightness > 1.0f) {
            batch.setColor(1, 1, 1, (brightness - 1.0f) * 0.5f);
            batch.draw(whiteOverlay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.setColor(Color.WHITE); 
        batch.end();
    }

    @Override
    public void dispose(){
        Assets.manager.dispose();
        super.dispose();
    }
}
