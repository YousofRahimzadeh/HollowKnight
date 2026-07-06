package Yousof.HollowKnight.Screen.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Yousof.HollowKnight.Screen.Modal;
import Yousof.HollowKnight.Enum.KeysSettings;
import Yousof.HollowKnight.Enum.CheatKeys;
import Yousof.HollowKnight.Utils.animation.InteractiveKnightActor;

public class GuideModal extends Modal {

    private Preferences preferences;

    public GuideModal() {
        super();
        preferences = Gdx.app.getPreferences("hollowknight");
        this.center();

        Texture bgTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png")); 
        this.setBackground(new TextureRegionDrawable(bgTexture));

        Table scrollContent = new Table(skin);
        scrollContent.center();

        float sectionSpacing = 30f;

        scrollContent.add(new Label("--- INTERACTIVE CHARACTER TEST ---", skin)).colspan(2).padBottom(5).row();
        Label hintLabel = new Label("[ Press & Hold your movement/action keys to animate the Knight ]", skin);
        hintLabel.setFontScale(0.85f);
        scrollContent.add(hintLabel).colspan(2).center().padBottom(15).row();

        InteractiveKnightActor controllableKnight = new InteractiveKnightActor();
        scrollContent.add(controllableKnight).size(160, 110).colspan(2).center().padBottom(55f).row();

        Table keysGrid = new Table();
        KeysSettings[] allBinds = KeysSettings.values();
        for (int i = 0; i < allBinds.length; i++) {
            KeysSettings bind = allBinds[i];
            int currentKey = preferences.getInteger("KEY_" + bind.name(), bind.getKey());
            String keyName = com.badlogic.gdx.Input.Keys.toString(currentKey);
            
            keysGrid.add(new Label(getCleanName(bind.name()) + ": " + keyName, skin)).left().width(240).pad(5);
            if ((i + 1) % 2 == 0) keysGrid.row();
        }
        scrollContent.add(keysGrid).colspan(2).center().padBottom(sectionSpacing).row();

        scrollContent.add(new Label("--- KNIGHT ABILITIES & MECHANICS ---", skin)).colspan(2).padBottom(10).row();
        
        Table mechanicsTable = new Table();
        mechanicsTable.defaults().left().padBottom(12);

        Label healthTitle = new Label("[ Health System - Masks ]", skin);
        Label healthDesc = new Label("Your life is measured in Masks. Holding the FOCUS key consumes collected SOUL to focus and repair broken masks safely.", skin);
        healthDesc.setWrap(true);

        Label soulTitle = new Label("[ SOUL Orb Gathering ]", skin);
        Label soulDesc = new Label("Strike enemies with your Nail weapon (SLASH) to harvest and fill the SOUL Orb. SOUL is required for spells and healing.", skin);
        soulDesc.setWrap(true);

        Label combatTitle = new Label("[ Core Abilities ]", skin);
        Label combatDesc = new Label("Dash: Tap the dash key for a quick horizontal dodge.\nNail Slash: Strike enemies to deal damage or down-slash to bounce (Pogo).", skin);
        combatDesc.setWrap(true);

        mechanicsTable.add(healthTitle).center().row();
        mechanicsTable.add(healthDesc).width(540).padBottom(10).row();
        mechanicsTable.add(soulTitle).center().row();
        mechanicsTable.add(soulDesc).width(540).padBottom(10).row();
        mechanicsTable.add(combatTitle).center().row();
        mechanicsTable.add(combatDesc).width(540).row();

        scrollContent.add(mechanicsTable).colspan(2).center().padBottom(sectionSpacing).row();

        scrollContent.add(new Label("--- AVAILABLE CHEAT CODES ---", skin)).colspan(2).padBottom(10).row();
        
        Table cheatsTable = new Table();
        cheatsTable.defaults().left().padBottom(8);

        CheatKeys[] allCheats = CheatKeys.values();
        for (int i = 0; i < allCheats.length; i++) {
            CheatKeys cheat = allCheats[i];
            String keyName = com.badlogic.gdx.Input.Keys.toString(cheat.getTriggerKey());
            String cheatDescription = getCheatDescription(cheat);
            
            Label cheatLabel = new Label("|| " + getCleanCheatName(cheat.name()) + " (Ctrl + " + keyName + ") : " + cheatDescription, skin);
            cheatLabel.setWrap(true);
            cheatsTable.add(cheatLabel).left().width(540).pad(2).row();
        }

        scrollContent.add(cheatsTable).colspan(2).center().padBottom(25).row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBack();
            }
        });

        ScrollPane scrollPane = new ScrollPane(scrollContent, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setVariableSizeKnobs(false); 
        this.add(scrollPane).width(760).height(700).center().padTop(10).row();
        this.add(backButton).width(160).padTop(20).center();
    }

    private String getCleanName(String enumName) {
        if (enumName.startsWith("KNIGHT")) {
            return enumName.substring(6);
        }
        return enumName;
    }

    private String getCleanCheatName(String cheatName) {
        StringBuilder sb = new StringBuilder();
        String[] tokens = cheatName.split("_");
        for (String token : tokens) {
            if (token.length() > 0) {
                sb.append(token.substring(0, 1).toUpperCase()).append(token.substring(1).toLowerCase()).append(" ");
            }
        }
        return sb.toString().trim();
    }

    private String getCheatDescription(CheatKeys cheat) {
        switch (cheat) {
            case BOSS_TELEPORT:
                return "Teleport instantly to the False Knight boss arena.";
            case NOCLIP:
                return "Fly through map constraints without animations or gravity.";
            case EMERGENCY_HEAL:
                return "Instantly recover health masks when in critical state.";
            case REFILL_SOUL:
                return "Fully charge your gathered SOUL vessel container.";
            case GOD_MODE:
                return "Activate invincibility from hazards, spikes, and enemies.";
            case INSTA_KILL:
                return "Execute a fatal strike to eliminate all active screen enemies.";
            default:
                return "";
        }
    }

    public void onBack() {
        hide();
    }
}