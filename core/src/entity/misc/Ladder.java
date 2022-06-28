package entity.misc;

import app.screen.BaseAssetManager;
import chunk.Chunk;
import chunk.world.CreateBodyCallable;
import chunk.world.EntityBodyBuilder;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import common.Clock;
import common.GameSettings;
import entity.Entity;
import entity.attributes.msc.Coordinates;
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
}
