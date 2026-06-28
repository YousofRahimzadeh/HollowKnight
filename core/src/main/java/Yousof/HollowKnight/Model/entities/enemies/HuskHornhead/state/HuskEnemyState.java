package Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Model.entities.enemies.HuskHornhead.HuskHornheadEnemy;


public abstract class HuskEnemyState {
    protected Animation<TextureRegion> currentAnimation;
    protected float stateTime;
    protected HuskHornheadEnemy enemy;
    protected Body body;

    public void enter(HuskHornheadEnemy enemy){
        stateTime = 0f;
        this.enemy = enemy;
        this.body = enemy.getBody();
    }
    public void update(float delta){
        stateTime += delta;
    }
    public void draw(Batch batch){}
    public void exit(){}
    public void drawEffects(Batch batch, float stateTime){}
}
