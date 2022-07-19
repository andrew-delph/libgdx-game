package core.entity.block;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import core.app.screen.assets.BaseAssetManager;
import core.chunk.Chunk;
import core.chunk.world.CreateBodyCallable;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.entity.Entity;
import core.entity.attributes.msc.Coordinates;
import java.util.UUID;

public abstract class EmptyBlock extends Block {
  public EmptyBlock(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  public synchronized CreateBodyCallable addWorld(Chunk chunk) {
    Entity myEntity = this;
    return new CreateBodyCallable() {
      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createEmptyBlockBody(world, chunk.chunkRange, myEntity);
      }
    };
  }
}
