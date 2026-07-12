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

import Yousof.HollowKnight.Enum.AchievementTypes;
import Yousof.HollowKnight.Enum.GameText;
import Yousof.HollowKnight.Manager.LocalizationManager;
import Yousof.HollowKnight.Screen.Modal;

public class AchievementsModal extends Modal {

    private Preferences preferences;

    public AchievementsModal() {
        super();
        preferences = Gdx.app.getPreferences("hollowknight_achievements");
        this.center();

        Texture bgTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png")); 
        this.setBackground(new TextureRegionDrawable(bgTexture));

        Table scrollContent = new Table(skin);
        scrollContent.center();

        scrollContent.add(new Label(LocalizationManager.get(GameText.ACHIEVEMENTS_HALL_HEADER), skin)).colspan(2).padBottom(20).row();

        Table achievementsTable = new Table();
        achievementsTable.defaults().left().padBottom(15);

        AchievementTypes[] allAchievements = AchievementTypes.values();
        for (int i = 0; i < allAchievements.length; i++) {
            AchievementTypes achievement = allAchievements[i];
            
            boolean isUnlocked = preferences.getBoolean("ACH_" + achievement.name(), false);

            Label titleLabel = new Label(achievement.getTitle(), skin);
            Label descLabel = new Label(achievement.getDescription(), skin);
            descLabel.setWrap(true);

            Label statusLabel;

            if (isUnlocked) {
                titleLabel.setColor(1f, 1f, 1f, 1f);
                descLabel.setColor(0.8f, 0.8f, 0.8f, 1f);
                statusLabel = new Label("[ " + LocalizationManager.get(GameText.ACHIEVEMENTS_UNLOCKED) + " ]", skin);
                statusLabel.setColor(0.2f, 0.8f, 0.2f, 1f);
            } else {
                titleLabel.setColor(0.4f, 0.4f, 0.4f, 0.7f);
                descLabel.setColor(0.3f, 0.3f, 0.3f, 0.6f);
                statusLabel = new Label("[ " + LocalizationManager.get(GameText.ACHIEVEMENTS_LOCKED) + " ]", skin);
                statusLabel.setColor(0.7f, 0.2f, 0.2f, 0.7f);
            }

            achievementsTable.add(titleLabel).row();
            achievementsTable.add(descLabel).width(500).row();
            achievementsTable.add(statusLabel).padBottom(10).row();
            
            if (i < allAchievements.length - 1) {
                achievementsTable.add(new Label("--------------------------------------------------", skin)).padBottom(10).row();
            }
        }

        scrollContent.add(achievementsTable).colspan(2).center().padBottom(25).row();

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

        this.add(scrollPane).width(760).height(500).center().padTop(10).row();
        this.add(backButton).width(160).padTop(20).center();
    }

    public void onBack() {
        hide();
    }
}