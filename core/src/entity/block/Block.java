package entity.block;

import app.screen.BaseAssetManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import common.Coordinates;
import common.GameSettings;
import entity.Entity;
import entity.EntityBodyBuilder;

public abstract class Block extends Entity {

  public static float staticHeight = 1;
  public static float staticWidth = 1;

  public Block(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
    this.zindex = 0;
    this.setHeight((int) (Block.staticHeight * GameSettings.PIXEL_SCALE));
    this.setWidth((int) (Block.staticWidth * GameSettings.PIXEL_SCALE));
  }

  @Override
  public synchronized int getUpdateTimeout() {
    return Integer.MAX_VALUE;
  }

  public abstract Body addWorld(World world);
}
