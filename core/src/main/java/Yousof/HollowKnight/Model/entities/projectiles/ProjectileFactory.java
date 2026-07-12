package Yousof.HollowKnight.Model.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Utils.animation.AnimationManager;

public class ProjectileFactory {

    public static Projectile createProjectile(String name , Vector2 position , boolean isFacingRight){
        GameSession game = GameController.getGame();
        Projectile projectile;
        switch (name) {
            case "SoulVengefulProjectile":
                projectile = new VengefulProjectile(game.getWorld(), position , isFacingRight , 5 , AnimationManager.KnightProjectile.create("SoulBall", PlayMode.LOOP_PINGPONG, 0.08f));
                game.getProjectiles().add(projectile);
                return projectile;
            case "ShadowVengefulProjectile":
                projectile = new VengefulProjectile(game.getWorld(), position , isFacingRight , 8 , AnimationManager.KnightProjectile.create("ShadowBall", PlayMode.LOOP_PINGPONG, 0.08f));
                game.getProjectiles().add(projectile);
                return projectile;
            case "WaveProjectile":
                projectile = new WaveProjectile(game.getWorld(), position , isFacingRight , 1 , AnimationManager.KnightProjectile.create("Shockwave", PlayMode.LOOP_PINGPONG, 0.08f));
                game.getProjectiles().add(projectile);
                return projectile;
            default:
                return null;
        }
    }
}
