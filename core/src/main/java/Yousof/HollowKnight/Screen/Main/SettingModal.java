package Yousof.HollowKnight.Screen.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import Yousof.HollowKnight.Screen.Modal;
import Yousof.HollowKnight.Enum.Settings;
import Yousof.HollowKnight.Enum.KeysSettings;

public class SettingModal extends Modal {
    private Preferences preferences;
    
    private Label musicVolLabel;
    private Label sfxVolLabel;
    private Label musicToggleLabel;
    private Label sfxToggleLabel;
    private Label brightnessLabel;
    private Label languageLabel;
    
    private Label[] keyLabels;
    
    private KeysSettings selectedKeyToChange = null;
    private int selectedLabelIndex = -1;

    private final String[] languages = {"English", "Spanish"};
    private int currentLangIndex = 0;

    public SettingModal() {
        super();
        preferences = Gdx.app.getPreferences("hollowknight");

        this.setFillParent(true);
        this.center();

        Texture myTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png")); 
        TextureRegionDrawable myDrawable = new TextureRegionDrawable(myTexture); 
        this.setBackground(myDrawable);

        Settings.musicVolume = preferences.getFloat("musicVolume", 0.5f);
        Settings.sfxVolume = preferences.getFloat("sfxVolume", 1.0f);
        Settings.musicOn = preferences.getBoolean("musicOn", true);
        Settings.sfxOn = preferences.getBoolean("sfxOn", true);
        Settings.brightness = preferences.getFloat("brightness", 0.9f);
        currentLangIndex = preferences.getInteger("languageIndex", 0);

        for (KeysSettings keyBind : KeysSettings.values()) {
            int savedKey = preferences.getInteger("KEY_" + keyBind.name(), keyBind.getKey());
            keyBind.setKey(savedKey);
        }

        Table container = new Table(skin);
        container.center();

        musicVolLabel = new Label("", skin);
        Slider musicSlider = new Slider(0f, 1f, 0.01f, false, skin);
        musicSlider.setValue(Settings.musicVolume);
        updateMusicVolText(Settings.musicVolume);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.musicVolume = musicSlider.getValue();
                preferences.putFloat("musicVolume", Settings.musicVolume);
                preferences.flush();
                updateMusicVolText(Settings.musicVolume);
            }
        });

        sfxVolLabel = new Label("", skin);
        Slider sfxSlider = new Slider(0f, 1f, 0.01f, false, skin);
        sfxSlider.setValue(Settings.sfxVolume);
        updateSFXVolText(Settings.sfxVolume);
        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.sfxVolume = sfxSlider.getValue();
                preferences.putFloat("sfxVolume", Settings.sfxVolume);
                preferences.flush();
                updateSFXVolText(Settings.sfxVolume);
            }
        });

        musicToggleLabel = new Label("", skin);
        updateMusicToggleText();
        musicToggleLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.musicOn = !Settings.musicOn;
                preferences.putBoolean("musicOn", Settings.musicOn);
                preferences.flush();
                updateMusicToggleText();
            }
        });

        sfxToggleLabel = new Label("", skin);
        updateSfxToggleText();
        sfxToggleLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.sfxOn = !Settings.sfxOn;
                preferences.putBoolean("sfxOn", Settings.sfxOn);
                preferences.flush();
                updateSfxToggleText();
            }
        });

        TextButton resetAudioBtn = new TextButton("Reset Audio", skin);
        resetAudioBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.musicVolume = 0.5f;
                Settings.sfxVolume = 1.0f;
                Settings.musicOn = true;
                Settings.sfxOn = true;
                musicSlider.setValue(0.5f);
                sfxSlider.setValue(1.0f);
                updateMusicVolText(0.5f);
                updateSFXVolText(1.0f);
                updateMusicToggleText();
                updateSfxToggleText();
                preferences.putFloat("musicVolume", 0.5f);
                preferences.putFloat("sfxVolume", 1.0f);
                preferences.putBoolean("musicOn", true);
                preferences.putBoolean("sfxOn", true);
                preferences.flush();
            }
        });


        brightnessLabel = new Label("Brightness: " + (int)(Settings.brightness * 100) + "%", skin);
        Slider brightnessSlider = new Slider(0.4f, 1.6f, 0.05f, false, skin);
        brightnessSlider.setValue(Settings.brightness);
        brightnessSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.brightness = brightnessSlider.getValue();
                brightnessLabel.setText("Brightness: " + (int)(Settings.brightness * 100) + "%");
                preferences.putFloat("brightness", Settings.brightness);
                preferences.flush();
            }
        });

        languageLabel = new Label("", skin);
        updateLanguageText();
        languageLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentLangIndex = (currentLangIndex + 1) % languages.length;
                preferences.putInteger("languageIndex", currentLangIndex);
                preferences.flush();
                updateLanguageText();
            }
        });

        // --- بخش کنترلرها (CONTROLS GRID) ---
        KeysSettings[] allBinds = KeysSettings.values();
        keyLabels = new Label[allBinds.length];
        Table controlsGrid = new Table();

        for (int i = 0; i < allBinds.length; i++) {
            final KeysSettings currentBind = allBinds[i];
            final int index = i;

            keyLabels[i] = new Label("", skin);
            updateKeyText(index, currentBind);

            keyLabels[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (selectedLabelIndex != -1) {
                        keyLabels[selectedLabelIndex].setColor(Color.WHITE);
                        updateKeyText(selectedLabelIndex, KeysSettings.values()[selectedLabelIndex]);
                    }
                    selectedKeyToChange = currentBind;
                    selectedLabelIndex = index;
                    keyLabels[index].setText(getCleanName(currentBind.name()) + ": [ Press Any Key... ]");
                    keyLabels[index].setColor(Color.YELLOW);
                }
            });

            controlsGrid.add(keyLabels[i]).left().width(220).pad(4);
            if ((i + 1) % 2 == 0) controlsGrid.row();
        }

        TextButton resetControlsBtn = new TextButton("Reset Controls", skin);
        resetControlsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (KeysSettings bind : KeysSettings.values()) {
                    preferences.remove("KEY_" + bind.name());
                }
                preferences.flush();
                
                KeysSettings.KNIGHTRIGHT.setKey(com.badlogic.gdx.Input.Keys.RIGHT);
                KeysSettings.KNIGHTLEFT.setKey(com.badlogic.gdx.Input.Keys.LEFT);
                KeysSettings.KNIGHTLOOKUP.setKey(com.badlogic.gdx.Input.Keys.UP);
                KeysSettings.KNIGHTLOOKDOWN.setKey(com.badlogic.gdx.Input.Keys.DOWN);
                KeysSettings.KNIGHTJUMP.setKey(com.badlogic.gdx.Input.Keys.Z);
                KeysSettings.KNIGHTATTACK.setKey(com.badlogic.gdx.Input.Keys.X);
                KeysSettings.KNIGHTFOCUS.setKey(com.badlogic.gdx.Input.Keys.A);
                KeysSettings.KNIGHTVENGEFUL.setKey(com.badlogic.gdx.Input.Keys.V);
                KeysSettings.KNIGHTSCREAM.setKey(com.badlogic.gdx.Input.Keys.S);
                KeysSettings.KNIGHTDASH.setKey(com.badlogic.gdx.Input.Keys.C);

                for (int i = 0; i < KeysSettings.values().length; i++) {
                    updateKeyText(i, KeysSettings.values()[i]);
                }
            }
        });


        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBack();
            }
        });


        float pad = 6f;
        container.add(new Label("--- AUDIO & DISPLAY ---", skin)).colspan(2).padBottom(10).row();
        container.add(musicVolLabel).left().padBottom(pad);
        container.add(musicSlider).width(220).fillX().padBottom(pad).row();
        container.add(sfxVolLabel).left().padBottom(pad);
        container.add(sfxSlider).width(220).fillX().padBottom(pad).row();
        container.add(musicToggleLabel).left().padBottom(pad);
        container.add(sfxToggleLabel).left().padBottom(pad).row();
        container.add(brightnessLabel).left().padBottom(pad);
        container.add(brightnessSlider).width(220).fillX().padBottom(pad).row();
        container.add(languageLabel).colspan(2).left().padBottom(8).row();
        container.add(resetAudioBtn).colspan(2).center().padBottom(15).row();

        container.add(new Label("--- CONTROLS ---", skin)).colspan(2).padBottom(10).row();
        container.add(controlsGrid).colspan(2).center().padBottom(8).row();
        container.add(resetControlsBtn).colspan(2).center().padBottom(15).row();
        
        container.add(backButton).colspan(2).width(160).center();

        this.add(container);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        
        if (selectedKeyToChange != null && selectedLabelIndex != -1) {
            for (int i = 0; i < 256; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    selectedKeyToChange.setKey(i);
                    preferences.putInteger("KEY_" + selectedKeyToChange.name(), i);
                    preferences.flush();
                    
                    keyLabels[selectedLabelIndex].setColor(Color.WHITE);
                    updateKeyText(selectedLabelIndex, selectedKeyToChange);
                    
                    selectedKeyToChange = null;
                    selectedLabelIndex = -1;
                    break;
                }
            }
        }
    }


    private void updateMusicVolText(float val) {
        musicVolLabel.setText("Music Vol: " + (int) (val * 100) + "%");
    }

    private void updateSFXVolText(float val) {
        sfxVolLabel.setText("SFX Vol: " + (int) (val * 100) + "%");
    }

    private void updateMusicToggleText() {
        musicToggleLabel.setText("Music: " + (Settings.musicOn ? "[ ON ]" : "[ OFF ]"));
    }

    private void updateSfxToggleText() {
        sfxToggleLabel.setText("SFX: " + (Settings.sfxOn ? "[ ON ]" : "[ OFF ]"));
    }

    private void updateLanguageText() {
        languageLabel.setText("Language: [ " + languages[currentLangIndex] + " ]");
    }

    private void updateKeyText(int labelIndex, KeysSettings bind) {
        String keyName = com.badlogic.gdx.Input.Keys.toString(bind.getKey());
        keyLabels[labelIndex].setText(getCleanName(bind.name()) + ": " + keyName);
    }

    private String getCleanName(String enumName) {
        if(enumName.startsWith("KNIGHT")) {
            return enumName.substring(6);
        }
        return enumName;
    }


    public void onBack() {
        hide();
    }
}