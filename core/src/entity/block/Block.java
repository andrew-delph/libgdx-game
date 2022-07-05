package entity.block;

import app.screen.BaseAssetManager;
import chunk.world.EntityBodyBuilder;
import common.Clock;
import common.GameSettings;
import entity.Entity;
import entity.attributes.msc.Coordinates;

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
