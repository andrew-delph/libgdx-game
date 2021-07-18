package entity.block;

import app.render.BaseAssetManager;
import common.Clock;
import entity.EntityBodyBuilder;

public class StoneBlock extends SolidBlock {
  public StoneBlock(
      Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
    this.textureName = "stone.png";
  }
}
