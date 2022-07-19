package core.entity.block;

import core.app.screen.assets.BaseAssetManager;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.entity.attributes.msc.Coordinates;

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
