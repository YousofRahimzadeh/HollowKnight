package Yousof.HollowKnight.Screen.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import Yousof.HollowKnight.Screen.Modal;

public class PauseModal extends Modal {

    public PauseModal() {
        super();

        this.setFillParent(true);
        this.center();

        Texture myTexture = new Texture(Gdx.files.internal("ui/modalBackgrounds.png")); 
        TextureRegionDrawable myDrawable = new TextureRegionDrawable(myTexture); 
        this.setBackground(myDrawable);

        TextButton btn1 = new TextButton("Continue", skin);
        TextButton btn2 = new TextButton("Cheat Codes", skin);
        TextButton btn3 = new TextButton("Setting", skin);
        TextButton btn4 = new TextButton("Save and Exit", skin);

        Table container = new Table(skin);
        container.center();
        container.add(btn1).pad(10f).uniformX().fillX().row();
        container.add(btn2).pad(10f).uniformX().fillX().row();
        container.add(btn3).pad(10f).uniformX().fillX().row();
        container.add(btn4).pad(10f).uniformX().fillX().row();

        this.add(container);


        btn1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onContinue();
            }
        });
        btn2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onSetting();
            }
        });
        btn3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onGuide();
            }
        });
        btn4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onExit();
            }
        });
    }

    public void onContinue(){}
    public void onSetting(){}
    public void onGuide(){}
    public void onExit(){}

}