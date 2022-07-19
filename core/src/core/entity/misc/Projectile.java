package core.entity.misc;

import core.app.screen.assets.BaseAssetManager;
import core.chunk.Chunk;
import core.chunk.world.CreateBodyCallable;
import core.chunk.world.EntityBodyBuilder;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.tools.javac.util.Pair;
import core.common.Clock;
import core.entity.Entity;
import core.entity.attributes.msc.Coordinates;
import java.util.UUID;

public class Projectile extends Entity {

  public Projectile(
      Clock clock,
      BaseAssetManager baseAssetManager,
      EntityBodyBuilder entityBodyBuilder,
      Coordinates coordinates) {
    super(clock, baseAssetManager, entityBodyBuilder, coordinates);
  }

  @Override
  public CreateBodyCallable addWorld(Chunk chunk) {
    Projectile myProjectile = this;
    return new CreateBodyCallable() {

      @Override
      protected Pair<UUID, Body> addWorld(World world) {
        return EntityBodyBuilder.createProjectileBody(world, chunk.chunkRange, myProjectile);
      }
    };
  }

  @Override
  public String getTextureName() {
    return "bullet.png";
  }
}
