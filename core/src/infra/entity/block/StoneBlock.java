package infra.entity.block;

import infra.common.Clock;
import infra.common.render.BaseAssetManager;

public class StoneBlock extends Block {
  public StoneBlock(Clock clock, BaseAssetManager baseAssetManager) {
    super(clock, baseAssetManager);
    this.textureName = "stone.png";
  }
}
