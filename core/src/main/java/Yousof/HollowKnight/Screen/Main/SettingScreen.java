package Yousof.HollowKnight.Screen.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import Yousof.HollowKnight.Main;
import Yousof.HollowKnight.Enum.Settings;
import Yousof.HollowKnight.Screen.AbstractScreen;
import Yousof.HollowKnight.Enum.KeysSettings;

public class SettingScreen extends AbstractScreen {
    private Preferences preferences;
    
    // اِلِمان‌های متنی صوتی و تصویری
    private Label musicVolLabel;
    private Label sfxVolLabel;
    private Label musicToggleLabel;
    private Label sfxToggleLabel;
    private Label brightnessLabel;
    private Label languageLabel;
    
    // آرایه‌ای از لیبل‌ها برای کلیدهای بازی
    private Label[] keyLabels;
    
    // مدیریت وضعیت بایندینگ کلیدها
    private KeysSettings selectedKeyToChange = null;
    private int selectedLabelIndex = -1;

    private final String[] languages = {"English", "Persian", "Spanish"};
    private int currentLangIndex = 0;

    @Override
    public void show() {
        super.show();
        preferences = Gdx.app.getPreferences("hollowknight");
        
        // ۱. لود کردن و همگام‌سازی صدا و گرافیک
        Settings.musicVolume = preferences.getFloat("musicVolume", 0.5f);
        Settings.sfxVolume = preferences.getFloat("sfxVolume", 1.0f);
        Settings.musicOn = preferences.getBoolean("musicOn", true);
        Settings.sfxOn = preferences.getBoolean("sfxOn", true);
        Settings.brightness = preferences.getFloat("brightness", 0.9f);
        currentLangIndex = preferences.getInteger("languageIndex", 0);

        // ۲. لود کردن دکمه‌ها از پرفرنسز و ست کردن روی KeysSettings (اگر قبلاً ذخیره شده باشند)
        for (KeysSettings keyBind : KeysSettings.values()) {
            int savedKey = preferences.getInteger("KEY_" + keyBind.name(), keyBind.getKey());
            keyBind.setKey(savedKey);
        }

        // ساخت جدول اصلی
        Table table = new Table();
        table.setFillParent(true);
        table.center().pad(25);

        // ==========================================
        // 🎵 بخش تنظیمات صدا (AUDIO)
        // ==========================================
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

        // ==========================================
        // 📺 بخش تصویر و زبان (VIDEO & LANGUAGE)
        // ==========================================
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

        // ==========================================
        // 🎮 بخش کنترلرها داینامیک بر اساس ENUM شما
        // ==========================================
        KeysSettings[] allBinds = KeysSettings.values();
        keyLabels = new Label[allBinds.length];
        
        // ساخت یک جدول فرعی مخصوص کلیدها تا چیدمانش فشرده و ۲ ستونه باشد
        Table controlsGrid = new Table();

        for (int i = 0; i < allBinds.length; i++) {
            final KeysSettings currentBind = allBinds[i];
            final int index = i;

            keyLabels[i] = new Label("", skin);
            updateKeyText(index, currentBind);

            // قابلیت کلیک مستقیم روی متن کلید برای تعویض آن
            keyLabels[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // اگر از قبل منتظر کلید دیگری بودیم، آن را به حالت عادی برگردان
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

            // چیدن کلیدها در ۲ ستون مجزا بغل هم (هر ۲ کلید یک ردیف جدید)
            controlsGrid.add(keyLabels[i]).left().width(260).pad(6);
            if ((i + 1) % 2 == 0) {
                controlsGrid.row();
            }
        }

        TextButton resetControlsBtn = new TextButton("Reset Controls", skin);
        resetControlsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // پاک کردن تنظیمات ذخیره شده برای برگشتن به هاردکد پیش‌فرض داخل Enum
                for (KeysSettings bind : KeysSettings.values()) {
                    preferences.remove("KEY_" + bind.name());
                }
                preferences.flush();
                
                // لود مجدد مقادیر اولیه اصلی جاوا
                // از آنجا که enum در جاوا مقداردهی اولیه سختی دارد، مقادیر پیش‌فرض اورجینال شما را اینجا ست می‌کنیم
                KeysSettings.KNIGHTUP.setKey(com.badlogic.gdx.Input.Keys.UP);
                KeysSettings.KNIGHTDOWN.setKey(com.badlogic.gdx.Input.Keys.DOWN);
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
                Main.getInstance().setScreen(new MainScreen());
            }
        });

        // ==========================================
        // 📐 تزریق المان‌ها به جدول اصلی صفحه با پدینگ مرتب
        // ==========================================
        table.add(new Label("[ AUDIO & DISPLAY ]", skin)).colspan(2).padBottom(10).center().row();
        table.add(musicVolLabel).left().padBottom(5);
        table.add(musicSlider).width(250).fillX().padBottom(5).row();
        table.add(sfxVolLabel).left().padBottom(5);
        table.add(sfxSlider).width(250).fillX().padBottom(5).row();
        table.add(musicToggleLabel).left().padBottom(5);
        table.add(sfxToggleLabel).left().padBottom(5).row();
        table.add(brightnessLabel).left().padBottom(5);
        table.add(brightnessSlider).width(250).fillX().padBottom(5).row();
        table.add(languageLabel).colspan(2).left().padBottom(10).row();
        table.add(resetAudioBtn).colspan(2).center().padBottom(20).row();

        table.add(new Label("[ GAME CONTROLS ]", skin)).colspan(2).padBottom(10).center().row();
        table.add(controlsGrid).colspan(2).center().padBottom(10).row();
        table.add(resetControlsBtn).colspan(2).center().padBottom(20).row();
        
        table.add(backButton).colspan(2).width(200).center();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        
        // گوش دادن زنده به کیبورد جهت جایگزینی کلید جدید در فریم جاری
        if (selectedKeyToChange != null && selectedLabelIndex != -1) {
            for (int i = 0; i < 256; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    // ۱. تغییر زنده مقدار کلید درون خود Enum شما!
                    selectedKeyToChange.setKey(i);
                    
                    // ۲. ذخیره دائمی کلید روی هارد دیسک
                    preferences.putInteger("KEY_" + selectedKeyToChange.name(), i);
                    preferences.flush();
                    
                    // ۳. بازگرداندن وضعیت گرافیکی لیبل به حالت عادی
                    keyLabels[selectedLabelIndex].setColor(Color.WHITE);
                    updateKeyText(selectedLabelIndex, selectedKeyToChange);
                    
                    // ریسِت کردن فلگ‌ها برای کلید بعدی
                    selectedKeyToChange = null;
                    selectedLabelIndex = -1;
                    break;
                }
            }
        }
    }

    // ==========================================
    // 📝 متدهای فرعی برای فرمت‌دهی متون منو
    // ==========================================
    private void updateMusicVolText(float val) {
        musicVolLabel.setText("Music Volume: " + (int) (val * 100) + "%");
    }

    private void updateSFXVolText(float val) {
        sfxVolLabel.setText("SFX Volume: " + (int) (val * 100) + "%");
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

    // متد کمکی برای حذف کلمه KNIGHT از اول اسم متغیرها جهت زیباتر شدن ظاهر منو
    private String getCleanName(String enumName) {
        if(enumName.startsWith("KNIGHT")) {
            return enumName.substring(6);
        }
        return enumName;
    }
}