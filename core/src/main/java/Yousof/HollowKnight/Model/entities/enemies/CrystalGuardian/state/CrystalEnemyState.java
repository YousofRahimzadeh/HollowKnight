package Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.state;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Model.entities.enemies.CrystalGuardian.CrystalGuardianEnemy;


public abstract class GrystalEnemyState {
    protected Animation<TextureRegion> currentAnimation;
    protected float stateTime;
    protected CrystalGuardianEnemy enemy;
    protected Body body;

    public void enter (CrystalGuardianEnemy enemy){
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
