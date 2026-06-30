package Yousof.HollowKnight.Model.entities.projectiles;

import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Model.GameStore;

public class ProjectileFactory {

    public static Projectile createProjectile(String name){
        GameStore game = GameController.getGame();
        switch (name) {
            case "VengefulProjectile":
                VengefulProjectile projectile = new VengefulProjectile(game.getWorld(), game.getKnight());
                game.getProjectiles().add(projectile);
                return projectile;
            default:
                return null;
        }
    }
}
