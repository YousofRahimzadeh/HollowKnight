package Yousof.HollowKnight.Model.entities.npc;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.npc.sensors.ZoteSurroundSensor;
import Yousof.HollowKnight.Model.entities.npc.state.ZoteIdleState;
import Yousof.HollowKnight.Model.entities.npc.state.ZoteState;

public class Zote extends Entitie{

    private ZoteSurroundSensor surroundSensor;
    private ZoteState currentState;
    private boolean facingRight = true;

    public Zote(World world , float x, float y) {
        surroundSensor = new ZoteSurroundSensor();
        createBody(world , x, y);
        changeState(new ZoteIdleState());
    }

    @Override
    public void update(float dt) {
        currentState.update(dt);
        
    }

    @Override
    public void draw(Batch batch) {
        currentState.draw(batch);
    }

    private void createBody(World world , float x, float y) {
        float hx = 25 / Constants.PPM;
        float hy = 45 / Constants.PPM;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        body = world.createBody(bdef);
        body.setUserData(this);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy);
        fdef.shape = shape;
        fdef.friction = 0f;
        fdef.filter.categoryBits = Constants.BIT_NPC;
        fdef.filter.maskBits = Constants.BIT_GROUND;
        body.createFixture(fdef).setUserData("Zote_main_body");
        shape.dispose();

        surroundSensor.createSensors(body, hx, hy);
        
    }

    public void changeState(ZoteState newState){
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(this);
    }

    @Override
    public void dispose() {        
    }


    public ZoteSurroundSensor getSurroundSensor() {
        return surroundSensor;
    }

    public void setSurroundSensor(ZoteSurroundSensor surroundSensor) {
        this.surroundSensor = surroundSensor;
    }

    public ZoteState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ZoteState currentState) {
        this.currentState = currentState;
    }
    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
    
}
