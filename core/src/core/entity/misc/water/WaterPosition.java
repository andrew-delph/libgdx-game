package core.entity.misc.water;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import core.common.Pair;
import core.app.screen.assets.BaseAssetManager;
import core.app.screen.assets.animations.AnimationManager;
import core.chunk.Chunk;
import core.chunk.world.CreateBodyCallable;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.entity.Entity;
import java.util.UUID;

public class WaterPosition extends Entity {
  public static float staticHeight = 0.3f;

  public WaterPosition(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public void render(AnimationManager animationManager, SpriteBatch batch) {
  }

  @Override
  public synchronized int getUpdateTimeout() {
    return Integer.MAX_VALUE;
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
