package entity.collision;

import chunk.Chunk;
import chunk.ChunkRange;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameSettings;
import common.GameStore;
import entity.Entity;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class RayCastService {

  @Inject GameStore gameStore;

  public Set<Entity> rayCast(Coordinates start, Coordinates end) {
    /*
     Returns all the entities of the bodies reached by the ray-cast;
    */
    Set<Entity> rayCastCollisions = new HashSet<>();
    if (start.getBase().equals(end.getBase())) return rayCastCollisions;

    for (ChunkRange chunkRange : getChunkRangesOnLine(start, end)) {
      Chunk chunk = gameStore.getChunk(chunkRange);
      if (chunk == null) continue;
      chunk
          .getWorldWrapper()
          .applyWorld(
              (World world) -> {
                RayCastCallback rayCastCallback =
                    (fixture, point, normal, fraction) -> {
                      fixture.getUserData();

                      if (fixture.getUserData() != null) {
                        CollisionPoint collisionPoint = (CollisionPoint) fixture.getUserData();
                        rayCastCollisions.add(collisionPoint.getEntity());
                      }
                      return 1;
                    };

                world.rayCast(rayCastCallback, start.toVector2(), end.toVector2());
              });
    }
    return rayCastCollisions;
  }

  public Set<ChunkRange> getChunkRangesOnLine(Coordinates start, Coordinates end) {
    Set<ChunkRange> chunkRanges = new HashSet<>();
    chunkRanges.add(new ChunkRange(start));
    chunkRanges.add(new ChunkRange(end));

    float m = (end.getYReal() - start.getYReal()) / (end.getXReal() - start.getXReal());
    float b = (start.getYReal()) - (m * start.getXReal());

    Function<Float, Float> calcY = x -> (m * x) + b; // y = mx+b
    Function<Float, Float> calcX = y -> (y - b) / m; // x = (y-b)/m

    for (int x : intersectionInRange(start.getXReal(), end.getXReal(), GameSettings.CHUNK_SIZE)) {
      float y = calcY.apply((float) x);
      Coordinates tCoordinates = new Coordinates(x, y);
      chunkRanges.add(new ChunkRange(tCoordinates));
    }

    for (int y : intersectionInRange(start.getYReal(), end.getYReal(), GameSettings.CHUNK_SIZE)) {
      float x = calcX.apply((float) y);
      Coordinates tCoordinates = new Coordinates(x, y);
      chunkRanges.add(new ChunkRange(tCoordinates));
    }

    return chunkRanges;
  }

  public List<Integer> intersectionInRange(float start, float end, int interval) {
    if (end < start) {
      return intersectionInRange(end, start, interval);
    }

    if ((int) start / interval == (int) end / interval) {
      if ((start < 0 && end > 0)) {
        return new LinkedList<>(Arrays.asList(0));
      } else return new LinkedList<>();
    }

    List<Integer> toReturn = intersectionInRange(start, end - interval, interval);
    if (end < 0) {
      toReturn.add((((int) end / interval) - 1) * interval);
    } else {
      toReturn.add(((int) end / interval) * interval);
    }

    return toReturn;
  }
}
