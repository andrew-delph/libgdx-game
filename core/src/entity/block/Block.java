package entity.block;

import app.screen.BaseAssetManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import common.GameSettings;
import entity.Entity;
import entity.EntityBodyBuilder;

public abstract class Block extends Entity {

  public static float staticHeight = 1;
  public static float staticWidth = 1;

  public Block(
      Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
    this.textureName = "badlogic.jpg";
    this.zindex = 0;
    this.setHeight((int) (Block.staticHeight * GameSettings.PIXEL_SCALE));
    this.setWidth((int) (Block.staticWidth * GameSettings.PIXEL_SCALE));
  }

  @Override
  public synchronized int getUpdateTimeout() {
    return Integer.MAX_VALUE;
  }

  @Override
  public Body getBody() {
    return this.body;
  }

  @Override
  public void setBody(Body body) {
    this.body = body;
  }

  public abstract Body addWorld(World world);
}
