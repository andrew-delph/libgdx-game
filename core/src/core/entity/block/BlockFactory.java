package core.entity.block;

import com.google.inject.Inject;
import core.app.screen.assets.BaseAssetManager;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.entity.controllers.factories.EntityControllerFactory;

public class BlockFactory {
  @Inject Clock clock;
  @Inject BaseAssetManager baseAssetManager;
  @Inject EntityBodyBuilder entityBodyBuilder;
  @Inject EntityControllerFactory entityControllerFactory;

  BlockFactory() {}

  public DirtBlock createDirt(Coordinates coordinates) {
    DirtBlock block =
        new DirtBlock(clock, baseAssetManager, entityBodyBuilder, coordinates.getBase());
    block.setEntityController(entityControllerFactory.createSolidBlockController(block));
    return block;
  }

  public StoneBlock createStone(Coordinates coordinates) {
    StoneBlock stoneBlock =
        new StoneBlock(clock, baseAssetManager, entityBodyBuilder, coordinates.getBase());
    stoneBlock.setEntityController(entityControllerFactory.createSolidBlockController(stoneBlock));
    return stoneBlock;
  }

  public SkyBlock createSky(Coordinates coordinates) {
    SkyBlock skyBlock =
        new SkyBlock(clock, baseAssetManager, entityBodyBuilder, coordinates.getBase());
    return skyBlock;
  }
}
