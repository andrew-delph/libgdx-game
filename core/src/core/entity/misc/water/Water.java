package core.entity.misc.water;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import core.app.screen.assets.BaseAssetManager;
import core.chunk.Chunk;
import core.chunk.world.CreateBodyCallable;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.common.GameSettings;
import core.entity.Entity;
import java.util.UUID;

public class Water extends Entity {

  public Water(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public synchronized void renderSync() {

    if (this.sprite == null) {
      this.sprite = new Sprite((Texture) this.baseAssetManager.get(this.getTextureName()));
      this.sprite.setSize(GameSettings.PIXEL_SCALE, GameSettings.PIXEL_SCALE);
      sprite.setAlpha(0.1f);
    }
    this.sprite.setPosition(
        this.getCoordinatesWrapper().getCoordinates().getXReal() * GameSettings.PIXEL_SCALE,
        this.getCoordinatesWrapper().getCoordinates().getYReal() * GameSettings.PIXEL_SCALE);
  }

  @Override
  public CreateBodyCallable addWorld(Chunk chunk) {
    return new CreateBodyCallable() {
      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createWater(world, chunk.chunkRange, Water.this);
      }
    };
  }

  @Override
  public String getTextureName() {
    return "water.png";
  }
}
