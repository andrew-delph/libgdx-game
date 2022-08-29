package core.entity.misc;

import core.app.screen.assets.BaseAssetManager;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.entity.Entity;

public class Sand extends Entity {

  public Sand(Clock clock, BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder, Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }


}
