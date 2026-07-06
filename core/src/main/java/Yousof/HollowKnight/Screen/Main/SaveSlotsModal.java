package Yousof.HollowKnight.Screen.Main;

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
import Yousof.HollowKnight.Screen.Modal;
import Yousof.HollowKnight.Screen.Game.GameScreen;
import Yousof.HollowKnight.Utils.save.GameData;
import Yousof.HollowKnight.Utils.save.SaveManager;

public class SaveSlotsModal extends Modal {


    public SaveSlotsModal() {
        super();
        this.center();

        Texture bgTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png")); 
        this.setBackground(new TextureRegionDrawable(bgTexture));

        Table contentTable = new Table(skin);
        contentTable.center();

        contentTable.add(new Label("--- SELECT YOUR JOURNEY ---", skin)).padBottom(30).row();

        // ساخت ۳ اسلات ذخیره بازی
        for (int slot = 1; slot <= 4; slot++) {
            final int slotNumber = slot;
            
            // لود کردن دیتای فایل JSON برای بررسی وجود داشتن سیو
            GameData slotData = SaveManager.loadGame(slotNumber);

            Table slotRow = new Table(skin);
            slotRow.left().pad(10);

            if (slotData != null) {
                String infoText = "Slot " + slotNumber + " [ Masks: " + slotData.currentMasks + "/" + slotData.maxMasks + " | Soul: " + slotData.currentSoul + " ]";
                Label infoLabel = new Label(infoText, skin);
                slotRow.add(infoLabel).width(350).left();

                TextButton loadButton = new TextButton("Load Game", skin);
                loadButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameController.loadGame(slotNumber);
                        Main.getInstance().setScreen(new GameScreen());
                        hide();
                    }
                });
                slotRow.add(loadButton).width(120).right().padLeft(20);
                
            } else {
                Label emptyLabel = new Label("Slot " + slotNumber + " [ Empty Slot ]", skin);
                emptyLabel.setColor(0.5f, 0.5f, 0.5f, 1f);
                slotRow.add(emptyLabel).width(350).left();

                TextButton newGameButton = new TextButton("New Game", skin);
                newGameButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameController.loadGame(slotNumber);
                        Main.getInstance().setScreen(new GameScreen());
                        hide();
                    }
                });
                slotRow.add(newGameButton).width(120).right().padLeft(20);
            }

            contentTable.add(slotRow).padBottom(15).row();
        }

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBack();
            }
        });
        
        contentTable.add(backButton).width(160).padTop(25).center();
        this.add(contentTable).width(700).height(450).center();
    }

    public void onBack() {
        hide();
    }
}
