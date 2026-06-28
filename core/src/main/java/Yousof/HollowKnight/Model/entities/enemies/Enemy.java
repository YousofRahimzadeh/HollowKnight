package Yousof.HollowKnight.Model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Model.entities.Entitie;
import Yousof.HollowKnight.Model.entities.knight.Knight;

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

    public void takeDamage(Knight knight){}
    
    public void applyKnockback(Body attackerBody) {}
}
