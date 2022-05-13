package entity.block;

import app.screen.BaseAssetManager;
import chunk.Chunk;
import chunk.world.CreateBodyCallable;
import chunk.world.EntityBodyBuilder;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import common.Clock;
import entity.Entity;
import entity.attributes.Coordinates;
import java.util.UUID;

public abstract class SolidBlock extends Block {
  public SolidBlock(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public synchronized CreateBodyCallable addWorld(Chunk chunk) {
    Entity myEntity = this;
    return new CreateBodyCallable() {
      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createSolidBlockBody(
            world, chunk.chunkRange, myEntity); // TODO test with Entity.this
      }
    };
  }
}
