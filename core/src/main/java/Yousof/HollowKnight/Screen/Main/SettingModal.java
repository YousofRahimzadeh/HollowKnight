package Yousof.HollowKnight.Screen.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Yousof.HollowKnight.Enum.GameText;
import Yousof.HollowKnight.Enum.KeysSettings;
import Yousof.HollowKnight.Enum.Settings;
import Yousof.HollowKnight.Enum.SupportedLanguages;
import Yousof.HollowKnight.Manager.LocalizationManager;
import Yousof.HollowKnight.Screen.Modal;

public class SettingModal extends Modal {

    // ── Persisted state ───────────────────────────────────────────────────────
    private final Preferences preferences;

    // ── Key-rebinding state (survives language rebuilds) ──────────────────────
    private KeysSettings selectedKeyToChange = null;
    private int          selectedLabelIndex  = -1;

    // Live references rebuilt on each buildContent() call
    private Label[]  keyLabels;

    // ─────────────────────────────────────────────────────────────────────────

    public SettingModal() {
        super();
        preferences = Gdx.app.getPreferences("hollowknight");

        this.setFillParent(true);
        this.center();

        Texture bgTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png"));
        this.setBackground(new TextureRegionDrawable(bgTexture));

        // Sync Settings from persisted preferences on first open
        Settings.musicVolume = preferences.getFloat("musicVolume", 0.5f);
        Settings.sfxVolume   = preferences.getFloat("sfxVolume",   1.0f);
        Settings.musicOn     = preferences.getBoolean("musicOn",   true);
        Settings.sfxOn       = preferences.getBoolean("sfxOn",     true);
        Settings.brightness  = preferences.getFloat("brightness",  0.9f);

        for (KeysSettings bind : KeysSettings.values()) {
            bind.setKey(preferences.getInteger("KEY_" + bind.name(), bind.getKey()));
        }

        buildContent();
    }

    // ── Content builder (called on init and on every language switch) ─────────

    /**
     * Clears and fully reconstructs the modal's child widgets using the
     * currently active language from {@link LocalizationManager}.
     * Sliders are initialised from the live {@link Settings} values, so
     * positions are preserved across rebuilds.
     */
    private void buildContent() {
        // Remove all existing children without touching fillParent / background
        this.clearChildren();

        // ── Sliders (need live references inside listeners) ────────────────
        Slider musicSlider      = new Slider(0f, 1f, 0.01f, false, skin);
        Slider sfxSlider        = new Slider(0f, 1f, 0.01f, false, skin);
        Slider brightnessSlider = new Slider(0.4f, 1.6f, 0.05f, false, skin);

        musicSlider.setValue(Settings.musicVolume);
        sfxSlider.setValue(Settings.sfxVolume);
        brightnessSlider.setValue(Settings.brightness);

        // ── Dynamic labels ─────────────────────────────────────────────────
        Label musicVolLabel   = new Label(musicVolText(),      skin);
        Label sfxVolLabel     = new Label(sfxVolText(),        skin);
        Label musicTogLabel   = new Label(musicToggleText(),   skin);
        Label sfxTogLabel     = new Label(sfxToggleText(),     skin);
        Label brightnessLabel = new Label(brightnessText(),    skin);

        // ── Slider listeners ───────────────────────────────────────────────
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.musicVolume = musicSlider.getValue();
                preferences.putFloat("musicVolume", Settings.musicVolume);
                preferences.flush();
                musicVolLabel.setText(musicVolText());
            }
        });

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.sfxVolume = sfxSlider.getValue();
                preferences.putFloat("sfxVolume", Settings.sfxVolume);
                preferences.flush();
                sfxVolLabel.setText(sfxVolText());
            }
        });

        brightnessSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.brightness = brightnessSlider.getValue();
                preferences.putFloat("brightness", Settings.brightness);
                preferences.flush();
                brightnessLabel.setText(brightnessText());
            }
        });

        // ── Toggle listeners ───────────────────────────────────────────────
        musicTogLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.musicOn = !Settings.musicOn;
                preferences.putBoolean("musicOn", Settings.musicOn);
                preferences.flush();
                musicTogLabel.setText(musicToggleText());
            }
        });

        sfxTogLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.sfxOn = !Settings.sfxOn;
                preferences.putBoolean("sfxOn", Settings.sfxOn);
                preferences.flush();
                sfxTogLabel.setText(sfxToggleText());
            }
        });

        // ── Reset Audio ────────────────────────────────────────────────────
        TextButton resetAudioBtn = new TextButton(LocalizationManager.get(GameText.SETTINGS_RESET_AUDIO), skin);
        resetAudioBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.musicVolume = 0.5f;
                Settings.sfxVolume   = 1.0f;
                Settings.musicOn     = true;
                Settings.sfxOn       = true;
                musicSlider.setValue(0.5f);
                sfxSlider.setValue(1.0f);
                musicVolLabel.setText(musicVolText());
                sfxVolLabel.setText(sfxVolText());
                musicTogLabel.setText(musicToggleText());
                sfxTogLabel.setText(sfxToggleText());
                preferences.putFloat("musicVolume",   0.5f);
                preferences.putFloat("sfxVolume",     1.0f);
                preferences.putBoolean("musicOn",     true);
                preferences.putBoolean("sfxOn",       true);
                preferences.flush();
            }
        });

        // ── Language toggle ────────────────────────────────────────────────
        SupportedLanguages current = LocalizationManager.getCurrentLanguage();
        String langBtnText = LocalizationManager.get(GameText.SETTINGS_LANGUAGE)
                + ": [ " + current.displayName + " ]";
        TextButton langBtn = new TextButton(langBtnText, skin , "default");
        langBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LocalizationManager.setLanguage(LocalizationManager.getNextLanguage());
                // Reset any pending key-rebind to avoid stale state after rebuild
                selectedKeyToChange = null;
                selectedLabelIndex  = -1;
                buildContent();   // instant in-place UI refresh
            }
        });

        // ── Controls grid ──────────────────────────────────────────────────
        KeysSettings[] allBinds = KeysSettings.values();
        keyLabels = new Label[allBinds.length];
        Table controlsGrid = new Table();

        for (int i = 0; i < allBinds.length; i++) {
            final KeysSettings bind  = allBinds[i];
            final int          index = i;

            keyLabels[i] = new Label(keyBindText(bind), skin);

            keyLabels[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (selectedLabelIndex != -1) {
                        keyLabels[selectedLabelIndex].setColor(Color.WHITE);
                        keyLabels[selectedLabelIndex].setText(
                                keyBindText(KeysSettings.values()[selectedLabelIndex]));
                    }
                    selectedKeyToChange = bind;
                    selectedLabelIndex  = index;
                    keyLabels[index].setText(
                            localizedKeyName(bind) + ": "
                            + LocalizationManager.get(GameText.SETTINGS_KEY_PRESS_HINT));
                    keyLabels[index].setColor(Color.YELLOW);
                }
            });

            controlsGrid.add(keyLabels[i]).left().width(230).pad(4);
            if ((i + 1) % 2 == 0) controlsGrid.row();
        }

        // ── Reset Controls ─────────────────────────────────────────────────
        TextButton resetCtrlBtn = new TextButton(LocalizationManager.get(GameText.SETTINGS_RESET_CONTROLS), skin);
        resetCtrlBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
                for (KeysSettings bind : KeysSettings.values()) {
                    preferences.remove("KEY_" + bind.name());
                }
                preferences.flush();
                selectedKeyToChange = null;
                selectedLabelIndex  = -1;
                for (int i = 0; i < keyLabels.length; i++) {
                    keyLabels[i].setColor(Color.WHITE);
                    keyLabels[i].setText(keyBindText(KeysSettings.values()[i]));
                }
            }
        });

        // ── Back ───────────────────────────────────────────────────────────
        TextButton backBtn = new TextButton(LocalizationManager.get(GameText.BACK), skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBack();
            }
        });

        // ── Assemble content table ─────────────────────────────────────────
        Table content = new Table(skin);
        content.center();
        float pad = 6f;

        content.add(new Label(LocalizationManager.get(GameText.SETTINGS_AUDIO_DISPLAY_HEADER), skin))
                .colspan(2).padBottom(10).row();

        content.add(musicVolLabel).left().padBottom(pad);
        content.add(musicSlider).width(220).fillX().padBottom(pad).row();

        content.add(sfxVolLabel).left().padBottom(pad);
        content.add(sfxSlider).width(220).fillX().padBottom(pad).row();

        content.add(musicTogLabel).left().padBottom(pad);
        content.add(sfxTogLabel).left().padBottom(pad).row();

        content.add(brightnessLabel).left().padBottom(pad);
        content.add(brightnessSlider).width(220).fillX().padBottom(pad).row();

        content.add(langBtn).colspan(2).center().padBottom(8).row();
        content.add(resetAudioBtn).colspan(2).center().padBottom(15).row();

        content.add(new Label(LocalizationManager.get(GameText.SETTINGS_CONTROLS_HEADER), skin))
                .colspan(2).padBottom(10).row();
        content.add(controlsGrid).colspan(2).center().padBottom(8).row();
        content.add(resetCtrlBtn).colspan(2).center().padBottom(15).row();

        content.add(backBtn).colspan(2).width(160).center();

        // ── Wrap in a scroll pane to prevent overflow on small screens ─────
        ScrollPane scrollPane = new ScrollPane(content, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollBarPositions(false, true);

        scrollPane.setVariableSizeKnobs(false);

        this.add(scrollPane).width(600).height(650).center();
    }

    // ── Key-rebind input polling ───────────────────────────────────────────────

    @Override
    public void act(float delta) {
        super.act(delta);
        if (selectedKeyToChange == null || selectedLabelIndex == -1) return;

        for (int keycode = 0; keycode < 256; keycode++) {
            if (Gdx.input.isKeyJustPressed(keycode)) {
                selectedKeyToChange.setKey(keycode);
                preferences.putInteger("KEY_" + selectedKeyToChange.name(), keycode);
                preferences.flush();

                keyLabels[selectedLabelIndex].setColor(Color.WHITE);
                keyLabels[selectedLabelIndex].setText(keyBindText(selectedKeyToChange));

                selectedKeyToChange = null;
                selectedLabelIndex  = -1;
                break;
            }
        }
    }

    // ── Text helpers (use live Settings + LocalizationManager) ────────────────

    private String musicVolText() {
        return LocalizationManager.get(GameText.SETTINGS_MUSIC_VOL)
                + ": " + (int)(Settings.musicVolume * 100) + "%";
    }

    private String sfxVolText() {
        return LocalizationManager.get(GameText.SETTINGS_SFX_VOL)
                + ": " + (int)(Settings.sfxVolume * 100) + "%";
    }

    private String musicToggleText() {
        return Settings.musicOn
                ? LocalizationManager.get(GameText.SETTINGS_MUSIC_ON)
                : LocalizationManager.get(GameText.SETTINGS_MUSIC_OFF);
    }

    private String sfxToggleText() {
        return Settings.sfxOn
                ? LocalizationManager.get(GameText.SETTINGS_SFX_ON)
                : LocalizationManager.get(GameText.SETTINGS_SFX_OFF);
    }

    private String brightnessText() {
        return LocalizationManager.get(GameText.SETTINGS_BRIGHTNESS)
                + ": " + (int)(Settings.brightness * 100) + "%";
    }

    private String keyBindText(KeysSettings bind) {
        return localizedKeyName(bind) + ": " + com.badlogic.gdx.Input.Keys.toString(bind.getKey());
    }

    private String localizedKeyName(KeysSettings bind) {
        switch (bind) {
            case KNIGHTRIGHT:   return LocalizationManager.get(GameText.KEY_RIGHT);
            case KNIGHTLEFT:    return LocalizationManager.get(GameText.KEY_LEFT);
            case KNIGHTLOOKUP:  return LocalizationManager.get(GameText.KEY_LOOK_UP);
            case KNIGHTLOOKDOWN:return LocalizationManager.get(GameText.KEY_LOOK_DOWN);
            case KNIGHTJUMP:    return LocalizationManager.get(GameText.KEY_JUMP);
            case KNIGHTATTACK:  return LocalizationManager.get(GameText.KEY_ATTACK);
            case KNIGHTFOCUS:   return LocalizationManager.get(GameText.KEY_FOCUS);
            case KNIGHTVENGEFUL:return LocalizationManager.get(GameText.KEY_VENGEFUL);
            case KNIGHTSCREAM:  return LocalizationManager.get(GameText.KEY_SCREAM);
            case KNIGHTDASH:    return LocalizationManager.get(GameText.KEY_DASH);
            default:            return bind.name();
        }
    }

    // ── Callback hook ─────────────────────────────────────────────────────────

    public void onBack() {
        hide();
    }
}
