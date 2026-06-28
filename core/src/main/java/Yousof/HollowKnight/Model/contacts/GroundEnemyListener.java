package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.GroundEnemy;

public class GroundEnemyListener implements ContactListener {

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
        if ((obstacle.getUserData() != null && 
            (obstacle.getUserData().equals("grounds") || 
            obstacle.getUserData().equals("wall"))) && 
            sensor.getUserData() instanceof String && 
            sensor.getBody().getUserData() instanceof GroundEnemy) {

            GroundEnemy enemy = (GroundEnemy) sensor.getBody().getUserData();
            String sensorData = (String) sensor.getUserData();
            
            if (sensorData.equals("GroundEnemy_sensor_wall_right")) {
                enemy.getSensors().rightWall += (isBegin) ? 1 : -1;
            } else if (sensorData.equals("GroundEnemy_sensor_wall_left")) {
                enemy.getSensors().leftWall += (isBegin) ? 1 : -1;
            } else if (sensorData.equals("GroundEnemy_sensor_cliff_right")) {
                enemy.getSensors().rightCliff += (isBegin) ? 1 : -1;
                if(isBegin) enemy.getSensors().rightCliffInitialized = true;
            } else if (sensorData.equals("GroundEnemy_sensor_cliff_left")) {
                enemy.getSensors().leftCliff += (isBegin) ? 1 : -1;
                if(isBegin) enemy.getSensors().leftCliffInitialized = true;
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}
}