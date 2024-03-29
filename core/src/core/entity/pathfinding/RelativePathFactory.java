package core.entity.pathfinding;

import com.google.inject.Inject;
import core.common.Coordinates;

public class RelativePathFactory {

  @Inject EdgeStore edgeStore;

  public RelativePath create(Coordinates source, Coordinates target) {
    return new RelativePath(edgeStore, source, target);
  }
}
