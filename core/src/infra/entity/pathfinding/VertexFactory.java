package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import infra.common.Coordinates;
import infra.entity.Entity;

public interface VertexFactory {

  Vertex createVertex(Entity entity, Coordinates position, Vector2 velocity);
}
