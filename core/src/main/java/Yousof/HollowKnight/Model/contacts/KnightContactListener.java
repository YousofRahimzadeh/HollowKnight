package Yousof.HollowKnight.Model.contacts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Yousof.HollowKnight.Model.entities.enemies.Enemy;
import Yousof.HollowKnight.Model.entities.knight.Knight;

public class KnightContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        surroundSensor(fa, fb, true);
        surroundSensor(fb, fa, true); 
        attackSensor(fa, fb, true);
        attackSensor(fb, fa, true); 
        screamSensor(fa, fb, true);
        screamSensor(fb, fa, true); 
        
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        surroundSensor(fa, fb, false);
        surroundSensor(fb, fa, false);
        attackSensor(fa, fb, false);
        attackSensor(fb, fa, false);
        screamSensor(fa, fb, false);
        screamSensor(fb, fa, false); 
        
    }

    private void surroundSensor(Fixture ground , Fixture sensor , boolean Begin){
        if ("Knight_ground_sensor".equals(sensor.getUserData()) && "grounds".equals(ground.getUserData())) {
            Knight knight = (Knight) sensor.getBody().getUserData();
            if (knight != null) {
                knight.getSurroundSensors().downSensor += (Begin) ? 1 : -1;
            }
        }else if ("Knight_sensor_wall_right".equals(sensor.getUserData()) && "grounds".equals(ground.getUserData())) {
            Knight knight = (Knight) sensor.getBody().getUserData();
            if (knight != null) {
                knight.getSurroundSensors().rightSensor += (Begin) ? 1 : -1;
            }
        }else if ("Knight_sensor_wall_left".equals(sensor.getUserData()) && "grounds".equals(ground.getUserData())) {
            Knight knight = (Knight) sensor.getBody().getUserData();
            if (knight != null) {
                knight.getSurroundSensors().leftSensor += (Begin) ? 1 : -1;
            }
        }
    }

    private void attackSensor(Fixture enemy , Fixture sensor , boolean Begin){
        if("knight_attack_up_sensor".equals(sensor.getUserData()) && "Enemy_main_body".equals(enemy.getUserData())){
            Enemy enem = (Enemy) enemy.getBody().getUserData();
            Knight knight = (Knight) sensor.getBody().getUserData();
            if(Begin) {
                knight.getAttackSensors().upSensor.add(enem);
            } else {
                knight.getAttackSensors().upSensor.remove(enem);
            }
        }else if("knight_attack_down_sensor".equals(sensor.getUserData()) && "Enemy_main_body".equals(enemy.getUserData())){
            Enemy enem = (Enemy) enemy.getBody().getUserData();
            Knight knight = (Knight) sensor.getBody().getUserData();
            if(Begin) {
                knight.getAttackSensors().downSensor.add(enem);
            } else {
                knight.getAttackSensors().downSensor.remove(enem);
            }
        }else if("knight_attack_right_sensor".equals(sensor.getUserData()) && "Enemy_main_body".equals(enemy.getUserData())){
            Enemy enem = (Enemy) enemy.getBody().getUserData();
            Knight knight = (Knight) sensor.getBody().getUserData();
            if(Begin) {
                knight.getAttackSensors().rightSensor.add(enem);
            } else {
                knight.getAttackSensors().rightSensor.remove(enem);
            }
        }else if("knight_attack_left_sensor".equals(sensor.getUserData()) && "Enemy_main_body".equals(enemy.getUserData())){
            Enemy enem = (Enemy) enemy.getBody().getUserData();
            Knight knight = (Knight) sensor.getBody().getUserData();
            if(Begin) {
                knight.getAttackSensors().leftSensor.add(enem);
            } else {
                knight.getAttackSensors().leftSensor.remove(enem);
            }
        }
    }

    private void screamSensor(Fixture enemy , Fixture sensor , boolean Begin){
        if("knight_scream_surround_sensor".equals(sensor.getUserData()) && "Enemy_main_body".equals(enemy.getUserData())){
            Enemy enem = (Enemy) enemy.getBody().getUserData();
            Knight knight = (Knight) sensor.getBody().getUserData();
            if(Begin) {
                knight.getScreamSensros().wholeSensor.add(enem);
            } else {
                knight.getScreamSensros().wholeSensor.remove(enem);
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
