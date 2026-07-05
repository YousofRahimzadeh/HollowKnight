package Yousof.HollowKnight.Screen.Game;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import Yousof.HollowKnight.Main;
import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Screen.AbstractScreen;
import Yousof.HollowKnight.Screen.MainScreen;
import Yousof.HollowKnight.Screen.Modal;

public class GameProcessor extends InputAdapter{

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.ESCAPE:
                onEscape();
                break;
            case Keys.I:
                onI();
                break;
        }
        return false;
    }

    public void onEscape(){
        ((GameScreen) Main.getInstance().getScreen()).setState(GameState.pause);
        Modal modal = new PauseModal(){
            @Override
            public void onContinue() {
                ((GameScreen) Main.getInstance().getScreen()).setState(GameState.run);
                ((AbstractScreen) Main.getInstance().getScreen()).getModalStack().getChildren().forEach(f -> f.remove());
            }

            @Override
            public void onSetting() {
                super.onSetting();
                this.hide(); 
    
                SettingModal settingModal = new SettingModal() {
                    @Override
                    public void onBack() {
                        super.onBack(); 
                        onEscape();
                    }
                };
                settingModal.show();
            }

            @Override
            public void onGuide() {
                // TODO Auto-generated method stub
                super.onGuide();
            }

            @Override
            public void onExit() {
                GameController.saveGame();
                Main.getInstance().setScreen(new MainScreen());
            }
        };
        ((AbstractScreen) Main.getInstance().getScreen()).getModalStack().getChildren().forEach(f -> f.remove());
        ((AbstractScreen) Main.getInstance().getScreen()).getModalStack().add(modal);
        modal.show();
    }
    public void onI(){
        ((GameScreen) Main.getInstance().getScreen()).setState(GameState.pause);
        Modal modal = new InventoryModal();
        ((AbstractScreen) Main.getInstance().getScreen()).getModalStack().getChildren().forEach(f -> f.remove());
        ((AbstractScreen) Main.getInstance().getScreen()).getModalStack().add(modal);
        modal.show();
    }

}
