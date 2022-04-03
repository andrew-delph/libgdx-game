package entity.misc;

import app.screen.BaseAssetManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import common.Coordinates;
import common.GameSettings;
import entity.Entity;
import entity.EntityBodyBuilder;

public class Ladder extends Entity {
  public Ladder(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
    this.zindex = 2;
    this.setWidth(GameSettings.PIXEL_SCALE);
    this.setHeight(GameSettings.PIXEL_SCALE);
  }

  @Override
  public String getTextureName() {
    return "ladder.png";
  }

  @Override
  public synchronized Body addWorld(World world) {
    return EntityBodyBuilder.createEmptyLadderBody(world, this.coordinates);
  }
}
