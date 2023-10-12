package core.entity.misc;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import core.app.screen.assets.BaseAssetManager;
import core.chunk.Chunk;
import core.chunk.world.CreateBodyCallable;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.Pair;
import core.entity.block.SolidBlock;
import java.util.UUID;

public class Sand extends SolidBlock {
  public static float staticHeight = 1f;
  public static float staticWidth = 1f;

  public Sand(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
    this.setHeight((int) (Sand.staticHeight * GameSettings.PIXEL_SCALE));
    this.setWidth((int) (Sand.staticWidth * GameSettings.PIXEL_SCALE));
  }

  @Override
  public synchronized CreateBodyCallable addWorld(Chunk chunk) {

    return new CreateBodyCallable() {

      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createSand(world, chunk.chunkRange, Sand.this);
      }
    };
  }

  @Override
  public synchronized int getUpdateTimeout() {
    return Integer.MAX_VALUE;
  }

  @Override
  public String getTextureName() {
    return "sand.png";
  }
}
