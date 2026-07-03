package Yousof.HollowKnight.Model.entities.projectiles;

import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Model.GameSession;

public class ProjectileFactory {

    public static Projectile createProjectile(String name){
        GameSession game = GameController.getGame();
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
