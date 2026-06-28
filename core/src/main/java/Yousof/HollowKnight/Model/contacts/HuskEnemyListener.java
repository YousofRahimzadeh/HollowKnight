package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.HuskHornheadEnemy;
import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.sensors.HuskSeeSensors;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class HuskEnemyListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        aroundSensor(fa, fb, true);
        aroundSensor(fb, fa, true); 

        checkSensorContact(fa, fb, true);
        checkSensorContact(fb, fa, true);
        
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        aroundSensor(fa, fb, false);
        aroundSensor(fb, fa, false); 

        checkSensorContact(fa, fb, false);
        checkSensorContact(fb, fa, false);
        
    }

    private void aroundSensor(Fixture knightt , Fixture sensor , boolean Begin){
        if ("HuskHornhead_knightRight_is_around".equals(sensor.getUserData()) && "Knight_main_body".equals(knightt.getUserData())) {
            Knight knight = (Knight) knightt.getBody().getUserData();
            HuskSeeSensors sentrySensor = ((HuskHornheadEnemy)sensor.getBody().getUserData()).getSeeSensors();
            sentrySensor.knightRight = (Begin) ? knight : null;
        }else if ("HuskHornhead_knightLeft_is_around".equals(sensor.getUserData()) && "Knight_main_body".equals(knightt.getUserData())) {
            Knight knight = (Knight) knightt.getBody().getUserData();
            HuskSeeSensors sentrySensor = ((HuskHornheadEnemy)sensor.getBody().getUserData()).getSeeSensors();
            sentrySensor.knightLeft = (Begin) ? knight : null;
        }
    }

    private void checkSensorContact(Fixture sensor, Fixture obstacle, boolean isBegin) {
        if ((obstacle.getUserData() != null && 
            (obstacle.getUserData().equals("grounds") || 
            obstacle.getUserData().equals("wall"))) && 
            sensor.getUserData() instanceof String && 
            sensor.getBody().getUserData() instanceof HuskHornheadEnemy) {

            HuskHornheadEnemy enemy = (HuskHornheadEnemy) sensor.getBody().getUserData();
            String sensorData = (String) sensor.getUserData();
            
            if (sensorData.equals("HuskHornhead_sensor_wall_right")) {
                enemy.getSurroundSensors().rightWall += (isBegin) ? 1 : -1;
            } else if (sensorData.equals("HuskHornhead_sensor_wall_left")) {
                enemy.getSurroundSensors().leftWall += (isBegin) ? 1 : -1;
            } else if (sensorData.equals("HuskHornhead_sensor_cliff_right")) {
                enemy.getSurroundSensors().rightCliff += (isBegin) ? 1 : -1;
                if(isBegin) enemy.getSurroundSensors().rightCliffInitialized = true;
            } else if (sensorData.equals("HuskHornhead_sensor_cliff_left")) {
                enemy.getSurroundSensors().leftCliff += (isBegin) ? 1 : -1;
                if(isBegin) enemy.getSurroundSensors().leftCliffInitialized = true;
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
