package Yousof.HollowKnight.Model.entities.enemies.FalseKnight.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Enum.Constants;
import Yousof.HollowKnight.Model.entities.enemies.FalseKnight.FalseKnightEnemy;

public class FalseKnightState {
    protected Animation<TextureRegion> currentAnimation;
    protected float stateTime;
    protected FalseKnightEnemy enemy;
    protected Body body;
    protected boolean entered = false;

    public void enter(FalseKnightEnemy enemy){
        stateTime = 0f;
        this.enemy = enemy;
        this.body = enemy.getBody();
        this.entered = true;
    }
    public void update(float delta){
        stateTime += delta;
    }
    public void reCreateBody(){
        while (body.getFixtureList().size > 0) {
            body.destroyFixture(body.getFixtureList().first());
        }   
    }
    public void exit(){}
    public void draw(Batch batch){}
    public void drawEffects(Batch batch, float stateTime){}
    public boolean isEntered(){
        return this.entered;
    }
}
