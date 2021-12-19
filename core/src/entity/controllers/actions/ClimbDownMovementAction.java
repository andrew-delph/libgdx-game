package entity.controllers.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import entity.collision.ladder.EntityLadderContact;

public class ClimbDownMovementAction extends ClimbUpMovementAction {
    public ClimbDownMovementAction(EntityLadderContact entityLadderContact) {
        super(entityLadderContact);
    }

    @Override
    public void apply(Body body) {
        body.setLinearVelocity(new Vector2(0, -5));
    }
}
