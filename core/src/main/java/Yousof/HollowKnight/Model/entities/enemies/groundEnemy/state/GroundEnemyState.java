package Yousof.HollowKnight.Model.entities.enemies.groundEnemy.state;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.GroundEnemy;


public abstract class GroundEnemyState {
    protected float stateTime;
    protected GroundEnemy enemy;
    protected Body body;

    public void enter(GroundEnemy enemy){
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
