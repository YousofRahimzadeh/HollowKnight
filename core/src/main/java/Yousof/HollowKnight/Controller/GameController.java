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
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state.FalseDeathState;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Model.entities.projectiles.Projectile;
import Yousof.HollowKnight.Screen.Game.GameScreen;
import Yousof.HollowKnight.Screen.Game.GameState;
import Yousof.HollowKnight.Screen.Game.VictoryModal;
import Yousof.HollowKnight.Utils.camera.CameraSession;
import Yousof.HollowKnight.Utils.save.GameData;
import Yousof.HollowKnight.Utils.save.SaveManager;

public class GameController {

    private static GameSession game;

    public static GameSession getGame() {
        return game;
    }

    public static void changeGame(int slot){
        GameData gameData = new GameData();
        String KnightPos = (GameSession.getInstance().isSpawnInEnd()) ? "KnightSpawnEnd" : "KnightSpawnStart";
        gameData.currentMapName = GameSession.getInstance().getNextMap();
        gameData.currentMasks = GameSession.getInstance().getKnight().getCurrentMasks();
        gameData.currentSoul = GameSession.getInstance().getKnight().getCurrentSoul();
        gameData.totalTimeElapsed = GameSession.getInstance().getTotalTimeElapsed();
        gameData.deathCount = GameSession.getInstance().getDeathCount();
        gameData.enemiesDefeated = GameSession.getInstance().getEnemiesDefeated();
        gameData.isFalseKnightDefeated = false;

        game = GameSession.createInstance();
        CameraSession.createInstance();

        TiledMap map = new TmxMapLoader().load(gameData.currentMapName.getFilePath());
        game.setMap(map); 

        World world = new World(new Vector2(0f , -9.8f), true);
        game.setWorld(world);

        loadStaticBodies();
        loadDynamicBodies();
        loadSensorsBodies();
        
        gameData.knightX = (float)map.getLayers().get("spawns").getObjects().get(KnightPos).getProperties().get("x");
        gameData.knightY = (float)map.getLayers().get("spawns").getObjects().get(KnightPos).getProperties().get("y");
        Knight currentKnight = new Knight(world, new Vector2(gameData.knightX, gameData.knightY));
        game.getKnight().dispose();
        game.setKnight(currentKnight);
        game.getKnight().setCurrentMasks(gameData.currentMasks);
        game.getKnight().setCurrentSoul(gameData.currentSoul);
        game.setMapName(gameData.currentMapName);
        game.setDeathCount(gameData.deathCount);
        game.setEnemiesDefeated(gameData.enemiesDefeated);
        game.setTotalTimeElapsed(gameData.totalTimeElapsed);
        game.setSlot(slot);
        
        Main.getInstance().setScreen(new GameScreen());
        
        loadContactListeners();

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
        loadSensorsBodies();
        loadContactListeners();

        game.getKnight().getBody().setTransform(gameData.knightX , gameData.knightY , 0f);
        game.getKnight().setCurrentMasks(gameData.currentMasks);
        game.getKnight().setCurrentSoul(gameData.currentSoul);
        game.setDeathCount(gameData.deathCount);
        game.setEnemiesDefeated(gameData.enemiesDefeated);
        game.setTotalTimeElapsed(gameData.totalTimeElapsed);
        game.setMapName(gameData.currentMapName);
        game.setSlot(slot);

    }

    public static void createGame(int slot){
        game = GameSession.createInstance();
        CameraSession.createInstance();

        GameMap currentMap = GameMap.CRYSTALPEAKS;
        TiledMap map = new TmxMapLoader().load(currentMap.getFilePath());
        game.setMap(map); 

        World world = new World(new Vector2(0f , -9.8f), true);
        game.setWorld(world);

        loadStaticBodies();
        loadDynamicBodies();
        loadSensorsBodies();
        loadContactListeners();

        game.setMapName(currentMap);
        game.setSlot(slot);


        SaveManager.saveGame(game, false, slot);
    }

    public static void updateGame(float delta){
        if(((GameScreen) Main.getInstance().getScreen()).getState() == GameState.pause) return;
        
        GameSession.getInstance().addTime(delta);

        if(GameSession.getInstance().getNextMap() != null){
            changeGame(GameSession.getInstance().getSlot());
        }

        GameSession.getInstance().getEnemies().forEach(e -> {
            if(e instanceof FalseKnightEnemy falseKnight
                    && falseKnight.getCurrentState() instanceof FalseDeathState deathState
                    && deathState.currentPhase == FalseDeathState.LeapPhase.IDLE) {
                GameScreen screen = (GameScreen) Main.getInstance().getScreen();
                if(screen.getState() != GameState.pause) {
                    screen.setState(GameState.pause);
                    VictoryModal modal = new VictoryModal();
                    modal.show();
                }
            }
        });

        CheatCodeManager.handleCheats(GameSession.getInstance().getKnight());

        game.getWorld().step(1/60f, 6, 2);

        for(Projectile projectile : game.getProjectiles()){
            projectile.update(delta);
        }
        
        for(Enemy enemy : game.getEnemies()){
            enemy.update(delta);
        }

        game.getKnight().update(delta);

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

    public static void drawGame(SpriteBatch batch, float delta){
        for(Enemy enemy : game.getEnemies()){
            enemy.draw(batch);
        }
        game.getKnight().draw(batch);
        
        for(Projectile projectile : game.getProjectiles()){
            projectile.draw(batch);
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

    private static void loadDynamicBodies(){
        for(MapObject object : game.getMap().getLayers().get("spawns").getObjects()){
            if(object.getName().equals("KnightSpawnStart")){
                Knight knight = new Knight(game.getWorld(), new Vector2((float)object.getProperties().get("x"), (float)object.getProperties().get("y")));
                game.setKnight(knight);
            }
            if(object.getName().equals("GroundEnemy")){
                Enemy enemy = EnemyFactory.createEnemy("Crawlid", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(enemy);
            }
            if(object.getName().equals("LaserEnemy")){
                Enemy enemy = EnemyFactory.createEnemy("CrystalGuardian", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(enemy);
            }
            if(object.getName().equals("FlyingEnemy")){
                Enemy enemy = EnemyFactory.createEnemy("WingedSentry", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(enemy);
            }
            if(object.getName().equals("FalseKnight")){
                Enemy nextEnemy = EnemyFactory.createEnemy("FalseKnight", game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(nextEnemy);
            }
            if(object.getName().equals("Zote")){
                Zote zote = new Zote( game.getWorld(), (float)object.getProperties().get("x"), (float)object.getProperties().get("y"));
                game.getEnemies().add(zote);
            }
        }
    }

    private static void loadSensorsBodies(){
        for(MapObject object : game.getMap().getLayers().get("sensors").getObjects()){
            if("Teleport".equals(object.getName())){
                createSensorRectangleBody(object, Constants.BIT_GROUND);
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
    private static void createSensorRectangleBody(MapObject object, short categoryBits) {
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
        fdef.isSensor = true;
        fdef.filter.categoryBits = categoryBits;
        fdef.filter.maskBits = Constants.BIT_KNIGHT;
        
        body.createFixture(fdef).setUserData(object.getProperties().get("goTo"));
        shape.dispose();
    }


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

    public static void saveGame(){
        GameSession game = GameSession.getInstance();
        SaveManager.saveGame(game, false, game.getSlot());
    }
    
    public static void disposeGame(){
        GameSession game = GameSession.getInstance();
        game.dispose();
    }
}