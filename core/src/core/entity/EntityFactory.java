package core.entity;

import com.google.inject.Inject;
import core.app.screen.assets.BaseAssetManager;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.entity.misc.Ladder;
import core.entity.misc.Orb;
import core.entity.misc.Projectile;
import core.entity.misc.Turret;

public class EntityFactory {

  @Inject Clock clock;
  @Inject BaseAssetManager baseAssetManager;
  @Inject EntityBodyBuilder entityBodyBuilder;

  @Inject
  EntityFactory() {}

  public Entity createEntity(Coordinates coordinates) {
    Entity entity = new Entity(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return entity;
  }

  public Ladder createLadder(Coordinates coordinates) {
    Ladder ladder = new Ladder(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return ladder;
  }

  public Projectile createProjectile(Coordinates coordinates) {
    Projectile projectile = new Projectile(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return projectile;
  }

  public Turret createTurret(Coordinates coordinates) {
    Turret turret = new Turret(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return turret;
  }

  public Orb createOrb(Coordinates coordinates) {
    Orb orb = new Orb(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return orb;
  }
}
