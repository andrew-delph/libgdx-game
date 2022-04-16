package entity.controllers.actions;

import com.badlogic.gdx.physics.box2d.Body;
import common.exceptions.ChunkNotFound;
import entity.Entity;

public interface EntityAction {

  void apply(Body body);

  Boolean isValid(Entity entity) throws ChunkNotFound;
}
