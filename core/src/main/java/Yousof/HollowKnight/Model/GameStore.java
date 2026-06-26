package Yousof.HollowKnight.Model;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import Yousof.HollowKnight.Model.entities.Projectile;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class GameStore implements Disposable{
    private TiledMap map;
	private World world;
	private Knight knight;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;

	public GameStore(TiledMap map , World world, Knight knight) {
		this.map = map;
		this.world = world;
		this.knight = knight;
		this.enemies = new ArrayList<>();
		this.projectiles = new ArrayList<>();
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

    public void dispose(){
        map.dispose();
    }
}
