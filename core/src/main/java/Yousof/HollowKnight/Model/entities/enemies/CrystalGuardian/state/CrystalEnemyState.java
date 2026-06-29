package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.CrystalGuardian;


public abstract class CrystalEnemyState {
    protected Animation<TextureRegion> currentAnimation;
    protected float stateTime;
    protected CrystalGuardian enemy;
    protected Body body;

    public void enter (CrystalGuardian enemy){
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
