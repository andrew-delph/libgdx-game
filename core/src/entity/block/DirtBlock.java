package entity.block;

import app.screen.BaseAssetManager;
import common.Clock;
import common.Coordinates;
import entity.EntityBodyBuilder;

public class DirtBlock extends SolidBlock {

  public DirtBlock(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public String getTextureName() {
    return "dirtblock.jpg";
  }
}
