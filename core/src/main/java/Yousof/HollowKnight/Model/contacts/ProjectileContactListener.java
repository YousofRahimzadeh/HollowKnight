package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Controller.GameController;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.projectiles.Projectile;

public class ProjectileContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        
        checkEnemyToProjectileContact(fa, fb);
        checkEnemyToProjectileContact(fb, fa);
        checkProjectileToGroundContact(fa, fb);
        checkProjectileToGroundContact(fb, fa);
    }

    private void checkProjectileToGroundContact(Fixture fixtureA, Fixture fixtureB) {
        if ("grounds".equals(fixtureA.getUserData()) && ("Vengeful_main_body".equals(fixtureB.getUserData()) || "Wave_main_body".equals(fixtureB.getUserData()))) {
            
            Projectile projectile = (Projectile) fixtureB.getBody().getUserData();
            if (!GameController.getGame().getToRemove().contains(projectile)) {
                GameController.getGame().getToRemove().add(projectile);
            }
        }
    }

    private void checkEnemyToProjectileContact(Fixture fixtureA, Fixture fixtureB) {
                
        if ("Enemy_main_body".equals(fixtureA.getUserData()) && "Vengeful_main_body".equals(fixtureB.getUserData())) {
            
            Enemy enemy = (Enemy) fixtureA.getBody().getUserData();
            Projectile projectile = (Projectile) fixtureB.getBody().getUserData();
            
            enemy.takeDamage(projectile.getBody(), projectile.getDamage() , 10f);
        }

        if("Knight_main_body".equals(fixtureA.getUserData()) && "Wave_main_body".equals(fixtureB.getUserData())){
            Knight knight = (Knight)fixtureA.getBody().getUserData();
            Projectile projectile = (Projectile) fixtureB.getBody().getUserData();

            knight.takeDamage(projectile.getBody(), projectile.getDamage());
        }
    }

    @Override public void endContact(Contact contact) {}
    @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
    @Override public void preSolve(Contact contact, Manifold oldManifold) {}
}