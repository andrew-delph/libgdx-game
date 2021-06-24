package infra.entity.block;

import com.google.inject.Inject;

import infra.common.Clock;
import infra.common.render.BaseAssetManager;

public class BlockFactory {
  @Inject
  Clock clock;
  @Inject
  BaseAssetManager baseAssetManager;

  BlockFactory(){

  }
  public Block create(){
    return new Block(clock, baseAssetManager);
  };

  public DirtBlock createDirt(){
    return new DirtBlock(clock, baseAssetManager);
  };

  public StoneBlock createStone(){
    return new StoneBlock(clock, baseAssetManager);
  };

  public SkyBlock createSky(){
    return new SkyBlock(clock, baseAssetManager);
  };
}
