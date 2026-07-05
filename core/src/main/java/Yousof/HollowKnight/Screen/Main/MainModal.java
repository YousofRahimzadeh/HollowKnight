package Yousof.HollowKnight.Screen.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import Yousof.HollowKnight.Main;
import Yousof.HollowKnight.Screen.Modal;
import Yousof.HollowKnight.Screen.Game.GameScreen;
import Yousof.HollowKnight.Screen.Game.SettingModal;

public class MainModal extends Modal {

    public MainModal() {
        super();
        // REMOVED: this.setFillParent(true) -> handled automatically by AbstractScreen's modalStack (Stack)
        this.center(); // Centers the cell alignments inside this root table

        // 1. Header logo setup (Added directly to 'this' table)
        Texture hollowKnightHeader = new Texture("Sprites/Menu/SpriteAtlasTexture-Title-2048x2048-fmt12.png");
        Image header = new Image(hollowKnightHeader);
        this.add(header).center().padTop(40f).row();

        // 2. Menu buttons initialization
        Table menus = new Table();
        TextButton btnStart = new TextButton("Start Game", skin);
        TextButton btnSetting = new TextButton("Setting", skin);
        TextButton btnGuide = new TextButton("Guide", skin);
        TextButton btnAchievements = new TextButton("Achievements", skin);
        TextButton btnQuit = new TextButton("Quit Game", skin);

        menus.defaults().space(15).width(280); 
        menus.add(btnStart).row();
        menus.add(btnSetting).row();
        menus.add(btnGuide).row();
        menus.add(btnAchievements).row();
        menus.add(btnQuit).row();

        // 3. Footer decorative layout setup
        Texture left = new Texture("Sprites/Menu/Hidden_Dreams_Logo.png");
        Image leftImg = new Image(left);
        Texture right = new Texture("Sprites/Menu/team_cherry_logo_main_menu.png");
        Image rightImg = new Image(right);

        Table bottomTable = new Table();
        bottomTable.pad(30);

        // Uniform columns keep the middle column perfectly centered geometrically
        bottomTable.add(leftImg).left().uniformX().expandX().padLeft(50f);
        bottomTable.add(menus).center().expandX();
        bottomTable.add(rightImg).right().uniformX().expandX().size(120).padRight(50f);

        // 4. Construct rows neatly directly onto 'this' (No containerTable wrapper)
        this.add(bottomTable).growX().expandY().bottom();
        this.padBottom(40f);

        // --- Click Listeners Configuration ---
        
        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GameScreen());
            }
        });

        btnSetting.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide(); 
                
                SettingModal settingModal = new SettingModal() {
                    @Override
                    public void onBack() {
                        super.onBack();
                        MainModal mainModal = new MainModal();
                        mainModal.show();
                    }
                };
                settingModal.show(); 
            }
        });

        btnQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
}