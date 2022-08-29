package core.entity.misc;

import core.app.screen.assets.BaseAssetManager;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.common.GameSettings;
import core.entity.Entity;

public class Sand extends Entity {
  public static float staticHeight = 1f;
  public static float staticWidth = 1f;

  public Sand(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
    this.setHeight((int) (Sand.staticHeight * GameSettings.PIXEL_SCALE));
    this.setWidth((int) (Sand.staticWidth * GameSettings.PIXEL_SCALE));
  }

  @Override
  public String getTextureName() {
    return "sand.png";
  }
}
