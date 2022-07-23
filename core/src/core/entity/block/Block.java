package core.entity.block;

import core.app.screen.assets.BaseAssetManager;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.common.GameSettings;
import core.entity.Entity;

public abstract class Block extends Entity {

  public static float staticHeight = 1;
  public static float staticWidth = 1;

  public Block(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
    this.zindex = 0;
    this.setHeight((int) (Block.staticHeight * GameSettings.PIXEL_SCALE));
    this.setWidth((int) (Block.staticWidth * GameSettings.PIXEL_SCALE));
  }

  @Override
  public synchronized int getUpdateTimeout() {
    return Integer.MAX_VALUE;
  }
}
