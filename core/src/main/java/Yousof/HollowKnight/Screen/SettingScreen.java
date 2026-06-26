package Yousof.HollowKnight.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import Yousof.HollowKnight.Main;

public class SettingScreen extends AbstractScreen {
    private Preferences preferences;
    private Label volumeLabel;
    private float volume;


    @Override
    public void show() {
        super.show();

        preferences = Gdx.app.getPreferences("hollowknight");
        volume = preferences.getFloat("soundVolume", 1f);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        volumeLabel = new Label("Sound Volume: " + (int) (volume * 100) + "%", skin);
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        TextButton backButton = new TextButton("Back", skin);

        volumeSlider.setValue(volume);
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateVolume(volumeSlider.getValue());
            }
        });

        table.add(volumeLabel).colspan(2).padBottom(20).row();
        table.add(volumeSlider).width(500).colspan(2).padBottom(20).row();
        table.add(backButton).width(200).colspan(2).pad(10).row();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new MainScreen());
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    private void updateVolume(float newVolume) {
        volume = MathUtils.clamp(newVolume, 0f, 1f);
        preferences.putFloat("soundVolume", volume);
        preferences.flush();
        volumeLabel.setText("Sound Volume: " + (int) (volume * 100) + "%");
        Gdx.input.vibrate(10000);
    }
}
