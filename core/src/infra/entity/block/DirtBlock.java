package infra.entity.block;

import infra.app.render.BaseAssetManager;
import infra.common.Clock;
import infra.entity.EntityBodyBuilder;

public class DirtBlock extends SolidBlock {
  public DirtBlock(
      Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
    this.textureName = "dirtblock.jpg";
  }
}
