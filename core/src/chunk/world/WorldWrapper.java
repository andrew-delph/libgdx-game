package chunk.world;

import chunk.world.exceptions.BodyNotFound;
import chunk.world.exceptions.DestroyBodyException;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.GameSettings;
import entity.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class WorldWrapper {
  private final World world;
  private final Map<UUID, Body> uuidBodyMap = new HashMap<>();

  public WorldWrapper(World world) {
    this.world = world;
  }

  public synchronized void tick() {
    world.step(
        GameSettings.WORLD_TIME_STEP,
        GameSettings.WORLD_VELOCITY_ITERATIONS,
        GameSettings.WORLD_POSITION_ITERATIONS);
  }

  public synchronized void addEntity(Entity entity) {
    Body theBody = entity.addWorld(world);
    uuidBodyMap.put(entity.uuid, theBody);
  }

  public synchronized void destroyEntity(Entity entity) throws DestroyBodyException {
    uuidBodyMap.remove(entity.uuid);
    try {
      world.destroyBody(getBody(entity));
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

  public synchronized void setPosition(Entity entity, Vector2 position) throws BodyNotFound {
    getBody(entity).setTransform(position, 0);
  }

  public synchronized Boolean hasBody(Entity entity) {
    try {
      getBody(entity);
    } catch (BodyNotFound e) {
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
