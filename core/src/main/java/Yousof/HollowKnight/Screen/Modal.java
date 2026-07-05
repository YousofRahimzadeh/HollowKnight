package Yousof.HollowKnight.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import Yousof.HollowKnight.Main;

public class Modal extends Table{

    protected Skin skin;
    public Modal(){
        skin = new Skin(Gdx.files.internal("ui/uiSkin.json"));
    }

    public void show(){
        Screen currentScreen = Main.getInstance().getScreen();
        if(currentScreen != null && currentScreen instanceof AbstractScreen screen){
            screen.getModalStack().add(this);
        }
    }
    public void hide(){
        this.remove();
    }
}