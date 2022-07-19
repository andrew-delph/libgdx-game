package core.entity.controllers.actions;

import com.badlogic.gdx.physics.box2d.Body;
import core.common.exceptions.ChunkNotFound;
import core.entity.Entity;

public interface EntityAction {

  void apply(Body body);

  Boolean isValid(Entity entity) throws ChunkNotFound;
}
