package Yousof.HollowKnight.Controller;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Main;
import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Enum.GameMap;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Model.contacts.CrystalEnemyListener;
import Yousof.HollowKnight.Model.contacts.FalseKnightListener;
import Yousof.HollowKnight.Model.contacts.FlyingEnemyListener;
import Yousof.HollowKnight.Model.contacts.GameContactListener;
import Yousof.HollowKnight.Model.contacts.GlobalContactListener;
import Yousof.HollowKnight.Model.contacts.GroundEnemyListener;
import Yousof.HollowKnight.Model.contacts.HuskEnemyListener;
import Yousof.HollowKnight.Model.contacts.KnightContactListener;
import Yousof.HollowKnight.Model.contacts.ProjectileContactListener;
import Yousof.HollowKnight.Model.contacts.ZoteContactListener;
import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.enemies.EnemyFactory;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Model.entities.projectiles.Projectile;
import Yousof.HollowKnight.Screen.Game.GameScreen;
import Yousof.HollowKnight.Screen.Game.GameState;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.save.GameData;
import Yousof.HollowKnight.Utils.save.SaveManager;

public class GameController {

    private static GameSession game;

    public static GameSession getGame() {
        return game;
    }

    public static void loadGame(int slot){
        game = GameSession.createInstance();
        CameraSession.createInstance();

        GameData gameData = SaveManager.loadGame(slot);
        if(gameData == null){
            createGame(slot);
            return;
        }

        TiledMap map = new TmxMapLoader().load(gameData.currentMapName.getFilePath());
        game.setMap(map); 

        World world = new World(new Vector2(0f , -9.8f), true);
        game.setWorld(world);

        loadStaticBodies();
        loadDynamicBodies();

        Vector2 spawnPos = new Vector2(gameData.knightX * Constants.PPM, gameData.knightY * Constants.PPM   );
        Knight knight = new Knight(world, spawnPos);
        knight.setCurrentMasks(gameData.currentMasks);
        knight.setCurrentSoul(gameData.currentSoul);
        game.setKnight(knight);

        game.setMapName(gameData.currentMapName);
        game.setSlot(slot);

        loadContactListeners();
    }

    public static void createGame(int slot){
        game = GameSession.createInstance();
        CameraSession.createInstance();

        GameMap currentMap = GameMap.TEST;
        TiledMap map = new TmxMapLoader().load(currentMap.getFilePath());
        game.setMap(map); 

        World world = new World(new Vector2(0f , -9.8f), true);
        game.setWorld(world);

        loadStaticBodies();
        loadDynamicBodies();

        Vector2 spawnPos = getSpawnPosition();
        Knight knight = new Knight(world, spawnPos);
        game.setKnight(knight);

        game.setMapName(currentMap);
        game.setSlot(slot);

        loadContactListeners();

        SaveManager.saveGame(knight, currentMap, false, slot);
    }

    public static void updateGame(float delta){
        if(((GameScreen) Main.getInstance().getScreen()).getState() == GameState.pause) return;

        CheatCodeManager.handleCheats(GameSession.getInstance().getKnight());

        game.getWorld().step(1/60f, 6, 2);

        for(Projectile projectile : game.getProjectiles()){
            projectile.update(delta);
        }
        
        for(Enemy enemy : game.getEnemies()){
            enemy.update(delta);
        }

        game.getKnight().update(delta);
        game.getZote().update(delta);

        Iterator<Entitie> removeIter = game.getToRemove().iterator();

        while(removeIter.hasNext()){
            Entitie entitie = removeIter.next();

            if(entitie.getBody() != null) {
                game.getWorld().destroyBody(entitie.getBody());
            }
            entitie.dispose();

            if(entitie instanceof Projectile) {
                game.getProjectiles().remove(entitie); 
            }
            removeIter.remove(); 
        }
    }

    public static void drawGame(SpriteBatch batch , float delta){
        for(Enemy enemy : game.getEnemies()){
            enemy.draw(batch);
        }
        game.getZote().draw(batch);
        game.getKnight().draw(batch);
        
        for(Projectile projectile : game.getProjectiles()){
            projectile.draw(batch);
        }
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
        if (game.getMap().getLayers().get("grounds") != null) {
            for(MapObject object : game.getMap().getLayers().get("grounds").getObjects()){
                if (object instanceof RectangleMapObject) {
                    createStaticRectangleBody(object, Constants.BIT_GROUND, "grounds");
                }
            }
        }

        if (game.getMap().getLayers().get("spikes") != null) {
            for(MapObject object : game.getMap().getLayers().get("spikes").getObjects()){
                if (object instanceof RectangleMapObject) {
                    createStaticRectangleBody(object, Constants.BIT_GROUND, "spikes");
                } 
                else if (object instanceof PolygonMapObject) {
                    createStaticPolygonBody(object, Constants.BIT_GROUND, "spikes");
                }
            }
        } else {
            System.out.println("Warning: 'spikes' layer not found in Tiled Map!");
        }
    }
    
    // متد کمکی برای ساخت آبجکت‌های فیزیکی مستطیلی استاتیک
    private static void createStaticRectangleBody(MapObject object, short categoryBits, String userData) {
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
        fdef.filter.categoryBits = categoryBits;
        
        body.createFixture(fdef).setUserData(userData);
        shape.dispose();
    }

    // متد کمکی برای ساخت آبجکت‌های فیزیکی چندضلعی (مثل تیغ‌های مثلثی شکل)
    private static void createStaticPolygonBody(MapObject object, short categoryBits, String userData) {
        Polygon mapPolygon = ((PolygonMapObject) object).getPolygon();
        float[] vertices = mapPolygon.getTransformedVertices();
        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            worldVertices[i] = vertices[i] / Constants.PPM;
        }

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = game.getWorld().createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);

        FixtureDef fdef = new FixtureDef();
        fdef.friction = 0f;
        fdef.shape = shape;
        fdef.filter.categoryBits = categoryBits;

        body.createFixture(fdef).setUserData(userData);
        shape.dispose();
    }

    private static void loadDynamicBodies(){
        for(MapObject object : game.getMap().getLayers().get("spawn").getObjects()){
            if(object.getName().equals("GroundEnemy")){
                Enemy enemy = EnemyFactory.createEnemy("Crawlid", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(enemy);
            }
            if(object.getName().equals("LaserEnemy")){
                Enemy enemy = EnemyFactory.createEnemy("CrystalGuardian", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(enemy);
            }
            if(object.getName().equals("FalseKnight")){
                Enemy nextEnemy = EnemyFactory.createEnemy("FalseKnight", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(nextEnemy);
            }
            if(object.getName().equals("Zote")){
                Zote zote = new Zote( game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.setZote(zote);
            }
        }
    }

    private static void loadContactListeners(){
        GameContactListener manager = new GameContactListener();
        manager.addListeners(new KnightContactListener());
        manager.addListeners(new GroundEnemyListener());
        manager.addListeners(new FlyingEnemyListener());
        manager.addListeners(new HuskEnemyListener());
        manager.addListeners(new CrystalEnemyListener());
        manager.addListeners(new GlobalContactListener());
        manager.addListeners(new FalseKnightListener());
        manager.addListeners(new ZoteContactListener());
        manager.addListeners(new ProjectileContactListener());
        
        game.getWorld().setContactListener(manager);
    }

    public static void saveGame(){
        GameSession game = GameSession.getInstance();
        SaveManager.saveGame(game.getKnight(), game.getMapName(), false, game.getSlot());
    }
    
    public static void disposeGame(){
        game.dispose();
    }
}