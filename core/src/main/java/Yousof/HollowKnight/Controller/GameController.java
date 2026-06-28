package Yousof.HollowKnight.Controller;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.GameStore;
import Yousof.HollowKnight.Model.contacts.FlyingEnemyListener;
import Yousof.HollowKnight.Model.contacts.GameContactListener;
import Yousof.HollowKnight.Model.contacts.GlobalContactListener;
import Yousof.HollowKnight.Model.contacts.GroundEnemyListener;
import Yousof.HollowKnight.Model.contacts.HuskEnemyListener;
import Yousof.HollowKnight.Model.contacts.KnightContactListener;
import Yousof.HollowKnight.Model.entities.Projectile;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.enemies.EnemyFactory;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class GameController {

    private static GameStore game;

    public static void loadGame(GameStore Context){
        game = Context;

        TiledMap map = new TmxMapLoader().load("untitled.tmx");
        game.setMap(map); 

        World world = new World(new Vector2(0f , -9.8f), true);
        game.setWorld(world);

        loadStaticBodies();
        loadDynamicBodies();

        Vector2 spawnPos = getSpawnPosition();
        Knight knight = new Knight(world, spawnPos , 20);
        game.setKnight(knight);

        loadContactListeners();
    }

    public static void updateGame(float delta){
        for(Projectile projectile : game.getProjectiles()){
            projectile.update(delta);
        }
        for(Enemy enemy : game.getEnemies()){
            enemy.update(delta);
        }
        game.getKnight().update(delta);
        game.getWorld().step(1/60f, 6, 2);
    }

    public static void drawGame(SpriteBatch batch){
        for(Projectile projectile : game.getProjectiles()){
            projectile.draw(batch);
        }
        for(Enemy enemy : game.getEnemies()){
            enemy.draw(batch);
        }
        game.getKnight().draw(batch);
    }

    private static Vector2 getSpawnPosition() {    
        try {
            MapObject spawnObject = game.getMap().getLayers().get("spawn").getObjects().get("KnightSpawn");
            float x = (float) spawnObject.getProperties().get("x");
            float y = (float) spawnObject.getProperties().get("y");
            return new Vector2(x, y);
        } catch (Exception e) {
            System.out.println("Failed to get spawn position");
            return new Vector2(0, 0);
        }
    }

    private static void loadStaticBodies(){
        for(MapObject object : game.getMap().getLayers().get("grounds").getObjects()){

            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                float x = (rect.getX() + rect.getWidth() / 2f) / Constants.PPM;
                float y = (rect.getY() + rect.getHeight() / 2f) / Constants.PPM;
                bdef.position.set(x, y);

                Body body = game.getWorld().createBody(bdef);

                PolygonShape shape = new PolygonShape();
                float hx = (rect.getWidth() / 2f) / Constants.PPM;
                float hy = (rect.getHeight() / 2f) / Constants.PPM;
                shape.setAsBox(hx, hy);

                FixtureDef fdef = new FixtureDef();
                fdef.friction = 0f;
                fdef.shape = shape;
                fdef.filter.categoryBits = Constants.BIT_GROUND;
                body.createFixture(fdef).setUserData("grounds");;
                shape.dispose();
            }
        }
    }
    
    private static void loadDynamicBodies(){
        for(MapObject object : game.getMap().getLayers().get("spawn").getObjects()){
            if(object.getName().equals("GroundSpawn")){
                Enemy enemy = EnemyFactory.createEnemy("HuskHornhead", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                // Enemy nextEnemy = EnemyFactory.createEnemy("WingedSentry", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(enemy);
                // game.getEnemies().add(nextEnemy);
            }
        }
    }

    private static void loadContactListeners(){
        GameContactListener manager = new GameContactListener();
        manager.addListeners(new KnightContactListener());
        manager.addListeners(new GroundEnemyListener());
        manager.addListeners(new FlyingEnemyListener());
        manager.addListeners(new HuskEnemyListener());
        manager.addListeners(new GlobalContactListener());
        
        game.getWorld().setContactListener(manager);
    }
}
