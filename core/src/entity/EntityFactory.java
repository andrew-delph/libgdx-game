package entity;

import app.screen.BaseAssetManager;
import chunk.world.EntityBodyBuilder;
import com.google.inject.Inject;
import common.Clock;
import common.Coordinates;
import entity.misc.Ladder;
import entity.misc.Projectile;
import entity.misc.Turret;

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
}
