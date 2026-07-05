package Yousof.HollowKnight.Model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Model.entities.Entitie;

public class Enemy extends Entitie{
    protected float knockbackTimer = 0;

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(Batch batch) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        
    }

    public void takeDamage(Body body , int how , float strength){}
    
}
