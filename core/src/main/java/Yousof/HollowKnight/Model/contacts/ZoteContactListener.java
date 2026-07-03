package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Model.entities.knight.Knight;
import Yousof.HollowKnight.Model.entities.npc.Zote;

public class ZoteContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        surroundSensors(fa, fb, true);
        surroundSensors(fb, fa, true);         
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        surroundSensors(fa, fb, false);
        surroundSensors(fb, fa, false);         
        
    }

    private void surroundSensors(Fixture knightt , Fixture sensor , boolean Begin){
        if("Knight_main_body".equals(knightt.getUserData()) && sensor.getUserData().equals("Zote_knight_nearby")) {
            Knight knight = (Knight) knightt.getBody().getUserData();
            Zote enemy = (Zote) sensor.getBody().getUserData();
            enemy.getSurroundSensor().knight = (Begin) ? knight : null;
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
