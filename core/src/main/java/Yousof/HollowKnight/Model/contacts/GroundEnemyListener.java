package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Model.entities.enemies.GroundEnemy;

public class GroundEnemyListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        checkSensorContact(fa, fb, true);
        checkSensorContact(fb, fa, true);
        
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        checkSensorContact(fa, fb, false);
        checkSensorContact(fb, fa, false);
        
    }

    private void checkSensorContact(Fixture sensor, Fixture obstacle, boolean isBegin) {
        if (obstacle.getUserData().equals("grounds") || obstacle.getUserData().equals("wall")) {

            if ("sensor".equals(sensor.getUserData())) {
                String type = (String) sensor.getUserData();
                if (sensor.getBody().getUserData() instanceof GroundEnemy) {
                    GroundEnemy enemy = (GroundEnemy) sensor.getBody().getUserData();
                    enemy.handleSensorContact(type, isBegin);
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub
        
    }
    
}
