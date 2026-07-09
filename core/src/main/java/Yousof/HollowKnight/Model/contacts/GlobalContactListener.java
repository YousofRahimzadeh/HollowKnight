package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Enum.GameMap;
import Yousof.HollowKnight.Model.GameSession;
import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class GlobalContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        
        checkEnemyToKnightContact(fa, fb);
        checkEnemyToKnightContact(fb, fa);
        mapTeleportSensors(fa, fb);
        mapTeleportSensors(fb, fa);
    }

    @Override
    public void endContact(Contact contact) {
    }

    private void checkEnemyToKnightContact(Fixture fixtureA, Fixture fixtureB) {
                
        if (fixtureA.getBody().getUserData() instanceof Enemy && !("Zote_main_body".equals(fixtureA.getUserData())) && fixtureB.getBody().getUserData() instanceof Knight && !fixtureA.isSensor() && !fixtureB.isSensor()) {
            
            Enemy enemy = (Enemy) fixtureA.getBody().getUserData();
            Knight knight = (Knight) fixtureB.getBody().getUserData();
            
            knight.takeDamage(enemy); 
        }

        if ("spikes".equals(fixtureA.getUserData()) && fixtureB.getBody().getUserData() instanceof Knight && !fixtureA.isSensor() && !fixtureB.isSensor()) {
            
            Knight knight = (Knight) fixtureB.getBody().getUserData();
            
            knight.takeDamage(fixtureA.getBody()); 
        }
    }

    private void mapTeleportSensors(Fixture fixtureA , Fixture fixtureB){
        if(fixtureA.getUserData().equals("CityOfTearsEnd") && fixtureB.getUserData().equals("Knight_main_body")){
            GameSession.getInstance().setNextMap(GameMap.CITYOFTEARS);
            GameSession.getInstance().setSpawnInEnd(true);
        }
        if(fixtureA.getUserData().equals("CrystalPeaksEnd") && fixtureB.getUserData().equals("Knight_main_body")){
            GameSession.getInstance().setNextMap(GameMap.CRYSTALPEAKS);
            GameSession.getInstance().setSpawnInEnd(true);
        }
        if(fixtureA.getUserData().equals("CityOfTearsStart") && fixtureB.getUserData().equals("Knight_main_body")){
            GameSession.getInstance().setNextMap(GameMap.CITYOFTEARS);
            GameSession.getInstance().setSpawnInEnd(false);
        }
        if(fixtureA.getUserData().equals("CrystalPeaksStart") && fixtureB.getUserData().equals("Knight_main_body")){
            GameSession.getInstance().setNextMap(GameMap.CRYSTALPEAKS);
            GameSession.getInstance().setSpawnInEnd(false);

        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}
}