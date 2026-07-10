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
import Yousof.HollowKnight.Enum.GameText;
import Yousof.HollowKnight.Manager.LocalizationManager;
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

        contentTable.add(new Label(LocalizationManager.get(GameText.SELECT_JOURNEY), skin)).padBottom(30).row();

        for (int slot = 1; slot <= 4; slot++) {
            final int slotNumber = slot;
            
            GameData slotData = SaveManager.loadGame(slotNumber);

            Table slotRow = new Table(skin);
            slotRow.left().pad(10);

            if (slotData != null) {
                String infoText = LocalizationManager.get(GameText.SAVE_SLOT_GAME) + " " + slotNumber
                        + " [ " + LocalizationManager.get(GameText.SAVE_SLOT_MASKS) + ": " + slotData.currentMasks + "/" + slotData.maxMasks
                        + " | " + LocalizationManager.get(GameText.SAVE_SLOT_SOUL)  + ": " + slotData.currentSoul + " ]";

                TextButton loadButton = new TextButton(infoText, skin);
                TextButton deleteButton = new TextButton(LocalizationManager.get(GameText.DELETE_GAME), skin);
                loadButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameController.loadGame(slotNumber);
                        Main.getInstance().setScreen(new GameScreen());
                        hide();
                    }
                });
                deleteButton.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        SaveManager.deleteGame(slotNumber);
                        hide();
                        SaveSlotsModal saveSlot = new SaveSlotsModal(){
                            @Override
                            public void onBack() {
                                super.onBack();
                                MainModal mainModal = new MainModal();
                                mainModal.show();
                            }
                        };
                        saveSlot.show();
                    }
                });
                slotRow.add(loadButton).left();
                slotRow.add(deleteButton).right().padLeft(30);
                
            } else {
                TextButton newGameButton = new TextButton(LocalizationManager.get(GameText.NEW_GAME), skin);
                newGameButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameController.loadGame(slotNumber);
                        Main.getInstance().setScreen(new GameScreen());
                        hide();
                    }
                });
                slotRow.add(newGameButton).center();
            }

            contentTable.add(slotRow).padBottom(15).row();
        }

        TextButton backButton = new TextButton(LocalizationManager.get(GameText.BACK), skin);
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
