package Yousof.HollowKnight.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import Yousof.HollowKnight.Main;
import Yousof.HollowKnight.Model.GameStore;

public class MainScreen extends AbstractScreen {

    @Override
    public void show() {
        super.show();

        Table table = new Table();
        table.setFillParent(true);

        Texture HollowKnightHeader = new Texture("Sprites/Menu/SpriteAtlasTexture-Title-2048x2048-fmt12.png");
        Image header = new Image(HollowKnightHeader);
        table.add(header).center().row();
        Table menus = new Table();
        table.bottom();
        Texture Left = new Texture("Sprites/Menu/Hidden_Dreams_Logo.png");
        Image LeftImg = new Image(Left);
        Texture Right = new Texture("Sprites/Menu/team_cherry_logo_main_menu.png");
        Image RightImg = new Image(Right);
        Table bottomTable = new Table();
        bottomTable.pad(50);
        bottomTable.add(LeftImg).left().width(100);
        bottomTable.add(menus).expandX().center();
        bottomTable.add(RightImg).right().size(100, 100);
        table.add(bottomTable).growX();



        TextButton btn = new TextButton("Start Game", skin);
        TextButton btn2 = new TextButton("Setting", skin);
        TextButton btn3 = new TextButton("Guide", skin);
        TextButton btn4 = new TextButton("Achievements", skin);
        TextButton btn5 = new TextButton("Quit Game", skin);

        menus.bottom();
        menus.defaults().space(10).width(200);
        menus.add(btn).growX().row();
        menus.add(btn2).growX().row();
        menus.add(btn3).growX().row();
        menus.add(btn4).growX().row();
        menus.add(btn5).growX().row();

        rootTable.addActor(table);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GameScreen(new GameStore(null, null , null)));
            }
        });

        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new SettingScreen());
            }
        });

        btn3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GuideScreen());
            }
        });

        btn3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new AchievementsScreen());
            }
        });

        btn5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);    
    }
}
