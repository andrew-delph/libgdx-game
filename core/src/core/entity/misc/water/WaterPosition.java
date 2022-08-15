package core.entity.misc.water;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import core.app.screen.assets.BaseAssetManager;
import core.chunk.Chunk;
import core.chunk.world.CreateBodyCallable;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.common.GameSettings;
import core.entity.Entity;
import java.util.UUID;

public class WaterPosition extends Entity {

  public WaterPosition(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
    this.setWidth((int) (GameSettings.PIXEL_SCALE * 0.5));
    this.setHeight((int) (GameSettings.PIXEL_SCALE * 0.5));
  }

  @Override
  public CreateBodyCallable addWorld(Chunk chunk) {
    return new CreateBodyCallable() {
      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createWaterPosition(world, chunk.chunkRange, WaterPosition.this);
      }
    };
  }
}
