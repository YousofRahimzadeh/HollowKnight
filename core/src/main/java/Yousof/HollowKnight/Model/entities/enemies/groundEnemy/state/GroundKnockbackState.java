package Yousof.HollowKnight.Model.entities.enemies.groundEnemy.state;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state.WingedIdleState;
import Yousof.HollowKnight.Model.entities.enemies.FlyingEnemy.state.WingedSentryState;
import Yousof.HollowKnight.Model.entities.enemies.groundEnemy.GroundEnemy;

public class GroundKnockbackState extends GroundEnemyState {

    private float duration = 0.2f;
    private float timer = 0f;
    private Body attackerBody;
    private float strength;

    public GroundKnockbackState(Body attackerBody, float strength) {
        this.attackerBody = attackerBody;
        this.strength = strength;
    }

    @Override
    public void enter(GroundEnemy enemy) {
        super.enter(enemy);
        this.timer = 0f;


        if (body != null && attackerBody != null) {
            Vector2 enemyPos = body.getPosition();
            Vector2 attackerPos = attackerBody.getPosition();
            Vector2 direction = new Vector2(enemyPos.x - attackerPos.x, enemyPos.y - attackerPos.y).nor();


            body.setLinearVelocity(0, 0);

            body.applyLinearImpulse(
                direction.x * strength,
                strength * 0.3f,
                body.getWorldCenter().x,
                body.getWorldCenter().y,
                true
            );
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        
        timer += delta;

        if (timer >= duration) {
            enemy.changeState(new GroundRunState());
        }
    }

    @Override
    public void exit() {
        
        if (body != null) {
            body.setLinearVelocity(0, 0);
        }
        super.exit();
    }
}