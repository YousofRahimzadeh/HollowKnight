package Yousof.HollowKnight.Model;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.npc.Zote;
import Yousof.HollowKnight.Model.entities.projectiles.Projectile;

public class GameSession implements Disposable{
	private static GameSession gameSession;
    private TiledMap map;
	private World world;
	private Zote zote;
	private Knight knight;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;
	private ArrayList<Entitie> toRemove;

	private GameSession(){
		enemies = new ArrayList<>();
		projectiles = new ArrayList<>();
		toRemove = new ArrayList<>();
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
		return zote;
	}
	public void setZote(Zote zote) {
		this.zote = zote;
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
}
