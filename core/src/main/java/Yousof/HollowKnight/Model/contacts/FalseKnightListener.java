package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.sensors.FalseNearbySensors;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class FalseKnightListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        gorzSesors(fa, fb, true);
        gorzSesors(fb, fa, true); 
        chargeRunSensors(fa, fb, true);
        chargeRunSensors(fb, fa, true); 
        middleSensors(fa, fb, true);
        middleSensors(fb, fa, true);
        groundSensors(fa, fb, true);
        groundSensors(fb, fa, true);
        
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        gorzSesors(fa, fb, false);
        gorzSesors(fb, fa, false); 
        chargeRunSensors(fa, fb, false);
        chargeRunSensors(fb, fa, false);
        middleSensors(fa, fb, false);
        middleSensors(fb, fa, false);
        groundSensors(fa, fb, false);
        groundSensors(fb, fa, false);
        
    }

    private void gorzSesors(Fixture knightt , Fixture sensor , boolean Begin){
        if("Knight_main_body".equals(knightt.getUserData()) && sensor.getUserData().equals("FalseKnight_knight_nearby")) {
            Knight knight = (Knight) knightt.getBody().getUserData();
            FalseKnightEnemy enemy = (FalseKnightEnemy) sensor.getBody().getUserData();
            enemy.getNearbySensors().knight = (Begin) ? knight : null;
        }
    }
    private void chargeRunSensors(Fixture knightt , Fixture sensor , boolean Begin){
        if("Knight_main_body".equals(knightt.getUserData()) && sensor.getUserData().equals("FalseKnight_knight_far")) {
            Knight knight = (Knight) knightt.getBody().getUserData();
            FalseKnightEnemy enemy = (FalseKnightEnemy) sensor.getBody().getUserData();
            enemy.getFarSensors().knight = (Begin) ? knight : null;
        }
    }

    private void middleSensors(Fixture knightt , Fixture sensor , boolean Begin){
        if("Knight_main_body".equals(knightt.getUserData()) && sensor.getUserData().equals("FalseKnight_knight_middle")) {
            Knight knight = (Knight) knightt.getBody().getUserData();
            FalseKnightEnemy enemy = (FalseKnightEnemy) sensor.getBody().getUserData();
            enemy.getMiddleSensors().knight = (Begin) ? knight : null;
        }
    }

    private void groundSensors(Fixture ground , Fixture sensor , boolean Begin){
        if("grounds".equals(ground.getUserData()) && sensor.getUserData().equals("FalseKnight_ground_sensor")) {
            FalseKnightEnemy enemy = (FalseKnightEnemy) sensor.getBody().getUserData();
            enemy.getGroundSensors().groundSensor += (Begin) ? 1 : -1;
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
