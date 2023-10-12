package core.entity.misc;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import core.app.screen.assets.BaseAssetManager;
import core.chunk.Chunk;
import core.chunk.world.CreateBodyCallable;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.common.GameSettings;
import core.common.Pair;
import core.entity.Entity;
import java.util.UUID;

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
  public synchronized CreateBodyCallable addWorld(Chunk chunk) {
    Ladder myLadder = this;
    return new CreateBodyCallable() {

      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createEmptyLadderBody(world, chunk.chunkRange, myLadder);
      }
    };
  }

  @Override
  public synchronized int getUpdateTimeout() {
    return Integer.MAX_VALUE;
  }
}
