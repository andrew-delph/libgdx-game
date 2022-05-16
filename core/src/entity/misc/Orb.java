package entity.misc;

import app.screen.BaseAssetManager;
import chunk.Chunk;
import chunk.world.CreateBodyCallable;
import chunk.world.EntityBodyBuilder;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import common.Clock;
import entity.Entity;
import entity.attributes.Coordinates;
import java.util.UUID;

public class Orb extends Entity {

  public Orb(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public String getTextureName() {
    return "orb.png";
  }

  @Override
  public synchronized CreateBodyCallable addWorld(Chunk chunk) {

    Orb myOrb = this;
    return new CreateBodyCallable() {

      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createOrb(world, chunk.chunkRange, myOrb);
      }
    };
  }
}