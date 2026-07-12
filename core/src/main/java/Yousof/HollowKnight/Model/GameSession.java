package Yousof.HollowKnight.Model;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import Yousof.HollowKnight.Enum.GameMap;
import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Model.entities.projectiles.Projectile;

public class GameSession implements Disposable{
	private static GameSession gameSession;
	private int slot;
	private GameMap mapName;
	private GameMap nextMap;
	private TiledMap map;
	private World world;
	private Knight knight;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;
	private ArrayList<Entitie> toRemove;
	private boolean spawnInEnd = false;

	private int deathCount = 0;
	private int enemiesDefeated = 0;
	private float totalTimeElapsed = 0f;

	private GameSession(){
		enemies = new ArrayList<>();
		projectiles = new ArrayList<>();
		toRemove = new ArrayList<>();
		nextMap = null;
	}

	public static GameSession getInstance(){
		if(gameSession == null){
			gameSession = new GameSession();
		}
		return gameSession;
	}

	public static GameSession createInstance(){
		gameSession = new GameSession();
		return gameSession;
	}

    public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}

	public Zote getZote() {
		for(Enemy enemy : enemies){
			if(enemy.getBody().getUserData() instanceof Zote zote){
				return zote;
			}
		}
		return null;
	}

	public FalseKnightEnemy getFalseKnight() {
		for(Enemy enemy : enemies){
			if(enemy.getBody().getUserData() instanceof FalseKnightEnemy falseKnight){
				return falseKnight;
			}
		}
		return null;
	}

	public Knight getKnight() {
		return knight;
	}

	public void setKnight(Knight knight) {
		this.knight = knight;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}

	public ArrayList<Entitie> getToRemove() {
		return toRemove;
	}
	public void addToRemove(Entitie toRemove) {
		this.toRemove.add(toRemove);
	}

    public void dispose(){
        map.dispose();
    }

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public GameMap getMapName() {
		return mapName;
	}

	public void setMapName(GameMap mapName) {
		this.mapName = mapName;
	}

	public GameMap getNextMap() {
		return nextMap;
	}

	public void setNextMap(GameMap nextMap) {
		this.nextMap = nextMap;
	}

	public boolean isSpawnInEnd() {
		return spawnInEnd;
	}

	public void setSpawnInEnd(boolean spawnInEnd) {
		this.spawnInEnd = spawnInEnd;
	}

	public int getDeathCount() {
		return deathCount;
	}

	public void incrementDeathCount() {
		this.deathCount++;
	}

	public int getEnemiesDefeated() {
		return enemiesDefeated;
	}

	public void incrementEnemiesDefeated() {
		this.enemiesDefeated++;
	}

	public float getTotalTimeElapsed() {
		return totalTimeElapsed;
	}

	public void addTime(float delta) {
		this.totalTimeElapsed += delta;
	}

	public void setDeathCount(int deathCount) {
		this.deathCount = deathCount;
	}

	public void setEnemiesDefeated(int enemiesDefeated) {
		this.enemiesDefeated = enemiesDefeated;
	}

	public void setTotalTimeElapsed(float totalTimeElapsed) {
		this.totalTimeElapsed = totalTimeElapsed;
	}
}
