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
import Yousof.HollowKnight.Enum.GameText;
import Yousof.HollowKnight.Manager.LocalizationManager;
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

        scrollContent.add(new Label(LocalizationManager.get(GameText.GUIDE_INTERACTIVE_HEADER), skin)).colspan(2).padBottom(5).row();
        Label hintLabel = new Label(LocalizationManager.get(GameText.GUIDE_INTERACTIVE_HINT), skin);
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
            
            keysGrid.add(new Label(localizedKeyName(bind) + ": " + keyName, skin)).left().width(240).pad(5);
            if ((i + 1) % 2 == 0) keysGrid.row();
        }
        scrollContent.add(keysGrid).colspan(2).center().padBottom(sectionSpacing).row();

        scrollContent.add(new Label(LocalizationManager.get(GameText.GUIDE_ABILITIES_HEADER), skin)).colspan(2).padBottom(10).row();
        
        Table mechanicsTable = new Table();
        mechanicsTable.defaults().left().padBottom(12);

        Label healthTitle = new Label(LocalizationManager.get(GameText.GUIDE_HEALTH_TITLE), skin);
        Label healthDesc = new Label(LocalizationManager.get(GameText.GUIDE_HEALTH_DESC), skin);
        healthDesc.setWrap(true);

        Label soulTitle = new Label(LocalizationManager.get(GameText.GUIDE_SOUL_TITLE), skin);
        Label soulDesc = new Label(LocalizationManager.get(GameText.GUIDE_SOUL_DESC), skin);
        soulDesc.setWrap(true);

        Label combatTitle = new Label(LocalizationManager.get(GameText.GUIDE_COMBAT_TITLE), skin);
        Label combatDesc = new Label(LocalizationManager.get(GameText.GUIDE_COMBAT_DESC), skin);
        combatDesc.setWrap(true);

        mechanicsTable.add(healthTitle).center().row();
        mechanicsTable.add(healthDesc).width(540).padBottom(10).row();
        mechanicsTable.add(soulTitle).center().row();
        mechanicsTable.add(soulDesc).width(540).padBottom(10).row();
        mechanicsTable.add(combatTitle).center().row();
        mechanicsTable.add(combatDesc).width(540).row();

        scrollContent.add(mechanicsTable).colspan(2).center().padBottom(sectionSpacing).row();

        scrollContent.add(new Label(LocalizationManager.get(GameText.GUIDE_CHEATS_HEADER), skin)).colspan(2).padBottom(10).row();
        
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

        TextButton backButton = new TextButton(LocalizationManager.get(GameText.BACK), skin);
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

    /** Converts SNAKE_CASE enum name to Title Case for display (e.g. BOSS_TELEPORT → Boss Teleport). */
    private String getCleanCheatName(String cheatName) {
        StringBuilder sb = new StringBuilder();
        for (String token : cheatName.split("_")) {
            if (!token.isEmpty()) {
                sb.append(Character.toUpperCase(token.charAt(0)))
                  .append(token.substring(1).toLowerCase())
                  .append(' ');
            }
        }
        return sb.toString().trim();
    }

    private String localizedKeyName(KeysSettings bind) {
        switch (bind) {
            case KNIGHTRIGHT:    return LocalizationManager.get(GameText.KEY_RIGHT);
            case KNIGHTLEFT:     return LocalizationManager.get(GameText.KEY_LEFT);
            case KNIGHTLOOKUP:   return LocalizationManager.get(GameText.KEY_LOOK_UP);
            case KNIGHTLOOKDOWN: return LocalizationManager.get(GameText.KEY_LOOK_DOWN);
            case KNIGHTJUMP:     return LocalizationManager.get(GameText.KEY_JUMP);
            case KNIGHTATTACK:   return LocalizationManager.get(GameText.KEY_ATTACK);
            case KNIGHTFOCUS:    return LocalizationManager.get(GameText.KEY_FOCUS);
            case KNIGHTVENGEFUL: return LocalizationManager.get(GameText.KEY_VENGEFUL);
            case KNIGHTSCREAM:   return LocalizationManager.get(GameText.KEY_SCREAM);
            case KNIGHTDASH:     return LocalizationManager.get(GameText.KEY_DASH);
            default:             return bind.name();
        }
    }

    private String getCheatDescription(CheatKeys cheat) {
        switch (cheat) {
            case BOSS_TELEPORT:    return LocalizationManager.get(GameText.CHEAT_DESC_BOSS_TELEPORT);
            case NOCLIP:           return LocalizationManager.get(GameText.CHEAT_DESC_NOCLIP);
            case EMERGENCY_HEAL:   return LocalizationManager.get(GameText.CHEAT_DESC_EMERGENCY_HEAL);
            case REFILL_SOUL:      return LocalizationManager.get(GameText.CHEAT_DESC_REFILL_SOUL);
            case GOD_MODE:         return LocalizationManager.get(GameText.CHEAT_DESC_GOD_MODE);
            case INSTA_KILL:       return LocalizationManager.get(GameText.CHEAT_DESC_INSTA_KILL);
            default:               return "";
        }
    }

    public void onBack() {
        hide();
    }
}