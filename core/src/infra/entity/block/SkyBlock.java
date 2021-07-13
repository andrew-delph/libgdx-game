package infra.entity.block;

import infra.common.Clock;
import infra.common.render.BaseAssetManager;
import infra.entity.EntityBodyBuilder;

public class SkyBlock extends EmptyBlock {
  public SkyBlock(
      Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
    this.textureName = "sky.png";
  }
}
