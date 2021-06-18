package infra.entity.pathfinding;

import com.google.inject.assistedinject.Assisted;

public interface PathFactory {

  Path createPath(@Assisted("source") Vertex source, @Assisted("target") Vertex target);
}
