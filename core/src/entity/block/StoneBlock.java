package entity.block;

import app.screen.BaseAssetManager;
import chunk.world.EntityBodyBuilder;
import common.Clock;
import common.Coordinates;

public class StoneBlock extends SolidBlock {
  public StoneBlock(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public String getTextureName() {
    return "stone.png";
  }
}
