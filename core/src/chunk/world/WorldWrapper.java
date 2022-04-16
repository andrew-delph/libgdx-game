package chunk.world;

import static common.GameSettings.GRAVITY;

import chunk.ChunkRange;
import chunk.world.exceptions.BodyNotFound;
import chunk.world.exceptions.DestroyBodyException;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import common.GameSettings;
import entity.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class WorldWrapper {
  private final World world = new World(new Vector2(0, -GRAVITY), false);
  private final Map<UUID, Body> uuidBodyMap = new HashMap<>();
  private final ChunkRange chunkRange;

  public WorldWrapper(ChunkRange chunkRange) {
    this.chunkRange = chunkRange;
  }

  public synchronized void tick() {
    world.step(
        GameSettings.WORLD_TIME_STEP,
        GameSettings.WORLD_VELOCITY_ITERATIONS,
        GameSettings.WORLD_POSITION_ITERATIONS);
  }

  public synchronized void addEntity(CreateBodyCallable createBodyCallable) {
    Pair<UUID, Body> pair = createBodyCallable.addWorld(world);
    if (pair == null) return;
    uuidBodyMap.put(pair.fst, pair.snd);
  }

  public synchronized void applyWorld(Consumer<World> worldConsumer) {
    worldConsumer.accept(this.world);
  }

  public synchronized void destroyEntity(Entity entity) throws DestroyBodyException {
    try {
      world.destroyBody(getBody(entity));
      uuidBodyMap.remove(entity.uuid);
    } catch (Exception e) {
      throw new DestroyBodyException(e.toString());
    }
  }

  public synchronized void applyBody(Entity entity, Consumer<Body> consumer) throws BodyNotFound {
    consumer.accept(getBody(entity));
  }

  public synchronized Vector2 getPosition(Entity entity) throws BodyNotFound {
    return getBody(entity).getPosition();
  }

  public synchronized Vector2 getVelocity(Entity entity) throws BodyNotFound {
    return getBody(entity).getLinearVelocity();
  }

  public synchronized void setActive(Entity entity, Boolean flag) throws BodyNotFound {
    getBody(entity).setActive(flag);
  }

  public synchronized void setGravity(Entity entity, Float gravity) throws BodyNotFound {
    getBody(entity).setGravityScale(gravity);
  }

  public synchronized void setPosition(Entity entity, Vector2 position) throws BodyNotFound {
    getBody(entity).setTransform(position, 0);
  }

  public synchronized void setVelocity(Entity entity, Vector2 velocity) throws BodyNotFound {
    getBody(entity).setLinearVelocity(velocity);
  }

  public synchronized Boolean hasBody(Entity entity) {
    try {
      getBody(entity);
    } catch (BodyNotFound e) {
      uuidBodyMap.remove(entity.uuid);
      return false;
    }
    return true;
  }

  private synchronized Body getBody(Entity entity) throws BodyNotFound {
    if (uuidBodyMap.get(entity.uuid) == null)
      throw new BodyNotFound("Body not found for entity: " + entity.uuid);
    return uuidBodyMap.get(entity.uuid);
  }
}
