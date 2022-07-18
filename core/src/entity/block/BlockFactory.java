package entity.block;

import app.screen.assets.BaseAssetManager;
import chunk.world.EntityBodyBuilder;
import com.google.inject.Inject;
import common.Clock;
import entity.attributes.msc.Coordinates;

public class BlockFactory {
  @Inject Clock clock;
  @Inject BaseAssetManager baseAssetManager;
  @Inject EntityBodyBuilder entityBodyBuilder;

  BlockFactory() {}

  public DirtBlock createDirt(Coordinates coordinates) {
    DirtBlock block = new DirtBlock(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return block;
  }

  public StoneBlock createStone(Coordinates coordinates) {
    StoneBlock stoneBlock = new StoneBlock(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return stoneBlock;
  }

  public SkyBlock createSky(Coordinates coordinates) {
    SkyBlock skyBlock = new SkyBlock(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return skyBlock;
  }
}
