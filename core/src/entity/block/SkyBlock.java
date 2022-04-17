package entity.block;

import app.screen.BaseAssetManager;
import chunk.world.EntityBodyBuilder;
import common.Clock;
import common.Coordinates;

public class SkyBlock extends EmptyBlock {
  public SkyBlock(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public String getTextureName() {
    if (this.coordinates.getY() < 0) {
      return "dirty.png";
    } else return "sky.png";
  }
}
