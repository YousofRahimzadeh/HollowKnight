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

        for (int slot = 1; slot <= 4; slot++) {
            final int slotNumber = slot;
            
            GameData slotData = SaveManager.loadGame(slotNumber);

            Table slotRow = new Table(skin);
            slotRow.left().pad(10);

            if (slotData != null) {
                String infoText = "Game " + slotNumber + " [ Masks: " + slotData.currentMasks + "/" + slotData.maxMasks + " | Soul: " + slotData.currentSoul + " ]";

                TextButton loadButton = new TextButton(infoText, skin);
                TextButton deleteButton = new TextButton("Delete Game", skin);
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
                TextButton newGameButton = new TextButton("New Game", skin);
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
