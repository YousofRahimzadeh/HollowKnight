package Yousof.HollowKnight.Model.entities.enemies;

import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Animations.Animations;

public class EnemyFactory {
    public static Enemy createEnemy(String type, World world, float x, float y) {
        switch (type) {
            case "Crawlid":
                return new GroundEnemy(world, x, y, 110f, 75f, 1.5f , 10 , Animations.Crawlid , 28f);
            case "Mosscreep":
                return new GroundEnemy(world, x, y, 110f, 95f, 1f , 15 , Animations.Mosscreep , 0f);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }
}
