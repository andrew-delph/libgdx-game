package core.entity.pathfinding;

import com.google.inject.Inject;
import core.entity.Entity;

public class PathGuiderFactory {
  @Inject RelativePathFactory relativePathFactory;

  @Inject
  PathGuiderFactory() {}

  public PathGuider createPathGuider(Entity entity) {
    return new PathGuider(relativePathFactory, entity);
  }
}
