package core.chunk.world;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import java.util.UUID;

public abstract class CreateBodyCallable {

  private World world;

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  protected abstract Pair<UUID, Body> addWorld(World world);
}
