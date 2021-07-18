package entity.controllers.actions;

import com.badlogic.gdx.physics.box2d.Body;

public interface EntityAction {

  void apply(Body body);

  Boolean isValid(Body body);
}
