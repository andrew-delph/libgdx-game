package core.entity;

import com.google.inject.Inject;
import core.app.screen.assets.BaseAssetManager;
import core.chunk.world.EntityBodyBuilder;
import core.common.Clock;
import core.common.Coordinates;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.misc.Ladder;
import core.entity.misc.Orb;
import core.entity.misc.Projectile;
import core.entity.misc.Sand;
import core.entity.misc.Turret;
import core.entity.misc.water.Water;
import core.entity.misc.water.WaterPosition;
import core.entity.statemachine.EntityStateMachineFactory;

public class EntityFactory {

  @Inject Clock clock;
  @Inject BaseAssetManager baseAssetManager;
  @Inject EntityBodyBuilder entityBodyBuilder;
  @Inject EntityStateMachineFactory entityStateMachineFactory;
  @Inject EntityControllerFactory entityControllerFactory;

  @Inject
  EntityFactory() {}

  public Entity createEntity(Coordinates coordinates) {
    Entity entity = new Entity(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return entity;
  }

  public Ladder createLadder(Coordinates coordinates) {
    Ladder ladder = new Ladder(clock, baseAssetManager, entityBodyBuilder, coordinates);
    ladder.setEntityController(entityControllerFactory.createLadderController(ladder));
    return ladder;
  }

  public Projectile createProjectile(Coordinates coordinates) {
    Projectile projectile = new Projectile(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return projectile;
  }

  public Turret createTurret(Coordinates coordinates) {
    Turret turret = new Turret(clock, baseAssetManager, entityBodyBuilder, coordinates);
    turret.setEntityController(entityControllerFactory.createTurretController(turret));
    return turret;
  }

  public Orb createOrb(Coordinates coordinates) {
    Orb orb = new Orb(clock, baseAssetManager, entityBodyBuilder, coordinates);
    orb.setEntityController(entityControllerFactory.createOrbController(orb));
    return orb;
  }

  public WaterPosition createWaterPosition(Coordinates coordinates) {
    WaterPosition waterPosition =
        new WaterPosition(clock, baseAssetManager, entityBodyBuilder, coordinates);
    waterPosition.setEntityController(
        entityControllerFactory.createWaterPositionController(waterPosition));
    return waterPosition;
  }

  public Water createWater(Coordinates coordinates) {
    Water water = new Water(clock, baseAssetManager, entityBodyBuilder, coordinates);
    water.setEntityController(entityControllerFactory.createWaterController(water));
    return water;
  }

  public Sand createSand(Coordinates coordinates) {
    Sand sand = new Sand(clock, baseAssetManager, entityBodyBuilder, coordinates);
    sand.setEntityController(entityControllerFactory.createSandController(sand));
    return sand;
  }
}
