package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.WingedSentry;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.sensors.WingedSentrySensor;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class FlyingEnemyListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        aroundSensor(fa, fb, true);
        aroundSensor(fb, fa, true); 
        
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        aroundSensor(fa, fb, false);
        aroundSensor(fb, fa, false); 
        
    }

    private void aroundSensor(Fixture knightt , Fixture sensor , boolean Begin){
        if ("WingedSentry_knight_is_around".equals(sensor.getUserData()) && "Knight_main_body".equals(knightt.getUserData())) {
            Knight knight = (Knight) knightt.getBody().getUserData();
            WingedSentrySensor sentrySensor = ((WingedSentry)sensor.getBody().getUserData()).getSensor();
            sentrySensor.knight = (Begin) ? knight : null;
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
