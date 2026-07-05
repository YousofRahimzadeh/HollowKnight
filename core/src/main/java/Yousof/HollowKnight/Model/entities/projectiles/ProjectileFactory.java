package Yousof.HollowKnight.Model.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class ProjectileFactory {

    public static Projectile createProjectile(String name){
        GameSession game = GameController.getGame();
        VengefulProjectile projectile;
        switch (name) {
            case "SoulVengefulProjectile":
                projectile = new VengefulProjectile(game.getWorld(), game.getKnight() , 5 , AnimationManager.KnightProjectile.create("SoulBall", PlayMode.LOOP_PINGPONG, 0.08f));
                game.getProjectiles().add(projectile);
                return projectile;
            case "ShadowVengefulProjectile":
                projectile = new VengefulProjectile(game.getWorld(), game.getKnight(), 8 , AnimationManager.KnightProjectile.create("ShadowBall", PlayMode.LOOP_PINGPONG, 0.08f));
                game.getProjectiles().add(projectile);
                return projectile;
            default:
                return null;
        }
    }
}
