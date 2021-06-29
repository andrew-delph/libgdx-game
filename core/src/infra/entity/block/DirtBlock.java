package infra.entity.block;

import infra.common.Clock;
import infra.common.render.BaseAssetManager;

public class DirtBlock extends Block {
  public DirtBlock(Clock clock, BaseAssetManager baseAssetManager) {
    super(clock, baseAssetManager);
    this.textureName = "dirtblock.jpg";
  }
}
