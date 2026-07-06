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

public class MainModal extends Modal {

    public MainModal() {
        super();
        this.center();

        Texture hollowKnightHeader = new Texture("Sprites/Menu/SpriteAtlasTexture-Title-2048x2048-fmt12.png");
        Image header = new Image(hollowKnightHeader);
        this.add(header).center().padTop(40f).row();

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

        Texture left = new Texture("Sprites/Menu/Hidden_Dreams_Logo.png");
        Image leftImg = new Image(left);
        Texture right = new Texture("Sprites/Menu/team_cherry_logo_main_menu.png");
        Image rightImg = new Image(right);

        Table bottomTable = new Table();
        bottomTable.pad(30);

        bottomTable.add(leftImg).left().uniformX().expandX().padLeft(50f);
        bottomTable.add(menus).center().expandX();
        bottomTable.add(rightImg).right().uniformX().expandX().size(120).padRight(50f);

        this.add(bottomTable).growX().expandY().bottom();
        this.padBottom(40f);
        
        btnStart.addListener(new ClickListener() {
            // @Override
            // public void clicked(InputEvent event, float x, float y) {
            //     Main.getInstance().setScreen(new GameScreen());
            // }
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                SaveSlotsModal slotsModal = new SaveSlotsModal(){
                    public void onBack() {
                        super.onBack();
                        MainModal mainModal = new MainModal();
                        mainModal.show();
                    }
                };
                slotsModal.show(); // باز کردن منوی اسلات‌ها روی استیج منو
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
        // Inside MainModal.java -> Click listeners configuration section
        btnGuide.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 1. Hide the current main menu modal interface
                hide(); 

                // 2. Create the Guide view with custom back button override functionality
                GuideModal guideModal = new GuideModal() {
                    @Override
                    public void onBack() {
                        super.onBack();
                        // When back is pressed inside guide, dismiss it and reload a clean MainModal
                        MainModal mainModal = new MainModal();
                        mainModal.show();
                    }
                };

                // 3. Mount the guide modal into the Stage
                guideModal.show(); 
            }
        });

        btnAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 1. Hide the current main menu modal interface
                hide(); 

                // 2. Create the Guide view with custom back button override functionality
                AchievementsModal achievementsModal = new AchievementsModal() {
                    @Override
                    public void onBack() {
                        super.onBack();
                        // When back is pressed inside guide, dismiss it and reload a clean MainModal
                        MainModal mainModal = new MainModal();
                        mainModal.show();
                    }
                };

                // 3. Mount the guide modal into the Stage
                achievementsModal.show(); 
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