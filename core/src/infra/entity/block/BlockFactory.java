package infra.entity.block;

import com.google.inject.Inject;
import infra.common.Clock;
import infra.app.render.BaseAssetManager;
import infra.entity.EntityBodyBuilder;

public class BlockFactory {
  @Inject Clock clock;
  @Inject BaseAssetManager baseAssetManager;
  @Inject EntityBodyBuilder entityBodyBuilder;

  BlockFactory() {}

  public DirtBlock createDirt() {
    return new DirtBlock(clock, baseAssetManager, entityBodyBuilder);
  }
  ;

  public StoneBlock createStone() {
    return new StoneBlock(clock, baseAssetManager, entityBodyBuilder);
  }
  ;

  public SkyBlock createSky() {
    return new SkyBlock(clock, baseAssetManager, entityBodyBuilder);
  }
  ;
}
