package Yousof.HollowKnight.Model.entities.enemies;

import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.CrystalGuardian;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.WingedSentry;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.HuskHornheadEnemy;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.GroundEnemy;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class EnemyFactory {
    public static Enemy createEnemy(String type, World world, float x, float y) {
        switch (type) {
            case "Crawlid":
                return new GroundEnemy(world, x, y, 110f, 75f, 1.5f , 10 , AnimationManager.Crawlid , 28f);
            case "HuskHornhead":
                return new HuskHornheadEnemy(world, x, y, 95f, 115f, 1.2f , 15 , AnimationManager.HuskHornhead , 28f);
            case "WingedSentry":
                return new WingedSentry(world, x, y);
            case "CrystalGuardian":
                return new CrystalGuardian(world, x, y , AnimationManager.CrystalGuardian , 20f);
            case "FalseKnight":
                return new FalseKnightEnemy(world, x, y);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }
}
