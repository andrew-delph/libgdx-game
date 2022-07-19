package core.entity.misc;

import core.app.screen.assets.BaseAssetManager;
import core.chunk.Chunk;
import core.chunk.world.CreateBodyCallable;
import core.chunk.world.EntityBodyBuilder;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import core.common.Clock;
import core.entity.Entity;
import core.entity.attributes.msc.Coordinates;
import java.util.UUID;

public class Orb extends Entity {

  boolean isLive = false;

  public Orb(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public String getTextureName() {
    return "orb.png";
  }

  public void needsUpdate() {
    isLive = true;
  }

  @Override
  public synchronized int getUpdateTimeout() {
    if (isLive) {
      isLive = false;
      return 1;
    }
    return Integer.MAX_VALUE;
  }

  @Override
  public synchronized CreateBodyCallable addWorld(Chunk chunk) {

    Orb myOrb = this;
    return new CreateBodyCallable() {

      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createOrb(world, chunk.chunkRange, myOrb);
      }
    };
  }
}
