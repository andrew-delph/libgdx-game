package infra.entity.pathfinding;

import com.google.inject.Inject;
import infra.entity.Entity;

public class PathGuiderFactory {
  @Inject RelativePathFactory relativePathFactory;

  @Inject
  PathGuiderFactory() {}

  public PathGuider createPathGuider(Entity entity) {
    return new PathGuider(relativePathFactory, entity);
  }
}
