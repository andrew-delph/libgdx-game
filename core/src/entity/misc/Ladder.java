package entity.misc;

import app.screen.BaseAssetManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import common.GameSettings;
import entity.Entity;
import entity.EntityBodyBuilder;

public class Ladder extends Entity {
  public Ladder(
      Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
    this.zindex = 2;
    this.textureName = "ladder.png";
    this.setWidth(GameSettings.COORDINATES_SCALE);
    this.setHeight(GameSettings.COORDINATES_SCALE);
  }

  @Override
  public synchronized Body addWorld(World world) {
    return EntityBodyBuilder.createEmptyLadderBody(world, this.coordinates);
  }
}
