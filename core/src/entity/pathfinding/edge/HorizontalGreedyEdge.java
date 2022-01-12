package entity.pathfinding.edge;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import common.Coordinates;
import entity.Entity;
import entity.pathfinding.EntityStructure;
import entity.pathfinding.RelativeCoordinates;
import entity.pathfinding.RelativePathNode;
import entity.pathfinding.RelativeVertex;

import static app.GameScreen.pathDebugRender;

public class HorizontalGreedyEdge extends AbstractEdge {
    RelativeCoordinates currentRelativeCoordinates;

    public HorizontalGreedyEdge(
            EntityStructure entityStructure, RelativeVertex from, RelativeVertex to) {
        super(entityStructure, from, to);
        this.currentRelativeCoordinates = from.getRelativeCoordinates();
    }

    @Override
    public Coordinates applyTransition(Coordinates sourceCoordinates) {
        return super.applyTransition(sourceCoordinates).getBase();
    }

    @Override
    public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
        return new HorizontalEdgeStepper();
    }

    @Override
    public void render(Coordinates position) {
        pathDebugRender.setColor(Color.GREEN);
        super.render(position);
    }
}

class HorizontalEdgeStepper extends EdgeStepper {

    @Override
    public void follow(Entity entity, RelativePathNode relativePathNode) throws Exception {
        String actionKey;

        if (!entity.coordinates.getBase().equals(relativePathNode.startPosition.getBase())
                && !entity.coordinates.getBase().equals(relativePathNode.getEndPosition().getBase())) {
            throw new Exception("not on track");
        }

        if (relativePathNode.getEndPosition().calcDistance(entity.coordinates) < 0.3) {
            Vector2 setBodyPosition = relativePathNode.getEndPosition().toVector2();
            entity.getBody().setTransform(setBodyPosition, 0);
            this.finish();
            return;
        }

        if (relativePathNode.getEndPosition().getXReal() + 0.1 > entity.coordinates.getXReal()) {
            actionKey = "right";
        } else if (relativePathNode.getEndPosition().getXReal() < entity.coordinates.getXReal()) {
            actionKey = "left";
        } else {
            return;
        }

        entity.entityController.applyAction(actionKey, entity.getBody());
    }
}
