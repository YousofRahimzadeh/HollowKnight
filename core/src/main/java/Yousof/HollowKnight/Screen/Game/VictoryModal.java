package Yousof.HollowKnight.Screen.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Yousof.HollowKnight.Main;
import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Enum.GameText;
import Yousof.HollowKnight.Manager.LocalizationManager;
import Yousof.HollowKnight.Screen.Modal;
import Yousof.HollowKnight.Screen.Main.MainScreen;
import Yousof.HollowKnight.Utils.audio.AudioManager;

public class VictoryModal extends Modal {

    private Texture bgTexture;

    public VictoryModal() {
        super();

        this.setFillParent(true);
        this.center();

        bgTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png"));
        this.setBackground(new TextureRegionDrawable(bgTexture));

        // Stop gameplay music and start victory theme
        AudioManager.getInstance().stopMusic();
        if (Gdx.files.internal("audio/victory.mp3").exists()) {
            AudioManager.getInstance().transitionToMusic("audio/victory.mp3", false);
        }

        // ── Title ──────────────────────────────────────────────
        Label titleLabel = new Label(LocalizationManager.get(GameText.VICTORY_TITLE), skin);
        titleLabel.setFontScale(1.4f);

        // ── Stats ──────────────────────────────────────────────
        GameSession session = GameSession.getInstance();

        int deaths = session.getDeathCount();
        int killed = session.getEnemiesDefeated();
        String timeFormatted = formatTime(session.getTotalTimeElapsed());

        Label deathLabel    = new Label(LocalizationManager.get(GameText.VICTORY_DEATHS)           + ":  " + deaths,        skin, "title");
        Label killsLabel    = new Label(LocalizationManager.get(GameText.VICTORY_ENEMIES_DEFEATED)  + ":  " + killed,        skin, "title");
        Label timeLabel     = new Label(LocalizationManager.get(GameText.VICTORY_TIME_ELAPSED)      + ":  " + timeFormatted,  skin, "title");
        
        // ── Buttons ────────────────────────────────────────────
        TextButton restartBtn  = new TextButton(LocalizationManager.get(GameText.RESTART),   skin);
        TextButton mainMenuBtn = new TextButton(LocalizationManager.get(GameText.MAIN_MENU), skin);

        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int slot = GameSession.getInstance().getSlot();
                GameController.disposeGame();
                GameController.createGame(slot);
                Main.getInstance().setScreen(new GameScreen());
            }
        });

        mainMenuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameController.createGame(GameSession.getInstance().getSlot());
                GameController.disposeGame();
                Main.getInstance().setScreen(new MainScreen());
                hide();
            }
        });

        // ── Layout ─────────────────────────────────────────────
        Table statsTable = new Table(skin);
        statsTable.defaults().left().padBottom(12f);
        statsTable.add(deathLabel).row();
        statsTable.add(killsLabel).row();
        statsTable.add(timeLabel).row();

        Table btnTable = new Table(skin);
        btnTable.defaults().width(220f).pad(8f);
        btnTable.add(restartBtn);
        btnTable.add(mainMenuBtn);

        Table content = new Table(skin);
        content.center();
        content.add(titleLabel).center().padBottom(24f).row();
        content.add(statsTable).center().padBottom(30f).row();
        content.add(btnTable).center();

        this.add(content).width(700f).height(460f).center();
    }

    private String formatTime(float totalSeconds) {
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void dispose() {
        if (bgTexture != null) {
            bgTexture.dispose();
            bgTexture = null;
        }
    }
}
