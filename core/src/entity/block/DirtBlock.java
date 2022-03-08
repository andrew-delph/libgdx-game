package entity.block;

import app.screen.BaseAssetManager;
import common.Clock;
import entity.EntityBodyBuilder;

public class DirtBlock extends SolidBlock {
  public DirtBlock(
      Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
    this.textureName = "dirtblock.jpg";
  }
}
