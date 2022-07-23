package core.entity.block;

import core.app.screen.assets.BaseAssetManager;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.entity.attributes.msc.Coordinates;

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
    if (this.getCoordinatesWrapper().getCoordinates().getY() < 0) {
      return "dirty.png";
    } else return "sky.png";
  }
}
