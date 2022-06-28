package app;

import app.user.User;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import chunk.world.exceptions.BodyNotFound;
import chunk.world.exceptions.DestroyBodyException;
import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import common.Direction;
import common.GameStore;
import common.events.EventService;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import entity.ActiveEntityManager;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.Attribute;
import entity.attributes.msc.Coordinates;
import entity.attributes.inventory.ItemNotFoundException;
import entity.attributes.inventory.item.OrbInventoryItem;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.DirtBlock;
import entity.block.EmptyBlock;
import entity.block.SkyBlock;
import entity.controllers.events.types.AbstractEntityEventType;
import entity.controllers.factories.EntityControllerFactory;
import entity.misc.Ladder;
import entity.misc.Orb;
import entity.misc.Projectile;
import entity.misc.Turret;
import java.util.UUID;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameController {

  final Logger LOGGER = LogManager.getLogger();
  @Inject GameStore gameStore;
  @Inject EntityFactory entityFactory;
  @Inject EventService eventService;
  @Inject EventTypeFactory eventTypeFactory;
  @Inject BlockFactory blockFactory;
  @Inject EntityControllerFactory entityControllerFactory;
  @Inject User user;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject ChunkFactory chunkFactory;

  public Entity addEntity(Entity entity) throws ChunkNotFound {
    triggerAddEntity(entity);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(entity.coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity triggerAddEntity(Entity entity) throws ChunkNotFound {
    this.gameStore.addEntity(entity);
    return entity;
  }

  public void removeEntity(UUID uuid) {
    Entity entity;
    try {
      entity = this.gameStore.getEntity(uuid);
    } catch (EntityNotFound e) {
      e.printStackTrace();
      return;
    }
    eventService.queuePostUpdateEvent(eventTypeFactory.createRemoveEntityEvent(uuid));
    eventService.fireEvent(
        EventTypeFactory.createRemoveEntityOutgoingEvent(
            entity.getUuid(), new ChunkRange(entity.coordinates)));
  }

  public Entity triggerRemoveEntity(UUID uuid) throws EntityNotFound, DestroyBodyException {
    return this.gameStore.removeEntity(uuid);
  }

  public Entity createEntity(Coordinates coordinates) throws ChunkNotFound {
    Entity entity = entityFactory.createEntity(coordinates);
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Block createSkyBlock(Coordinates coordinates) throws ChunkNotFound {
    Block entity = blockFactory.createSky(coordinates);
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Block createDirtBlock(Coordinates coordinates) throws ChunkNotFound {
    Block entity = blockFactory.createDirt(coordinates);
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Block createStoneBlock(Coordinates coordinates) throws ChunkNotFound {
    Block entity = blockFactory.createStone(coordinates);
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity createLadder(Coordinates coordinates) throws ChunkNotFound {
    try {
      if (!(this.gameStore.getBlock(coordinates) instanceof EmptyBlock)) {
        LOGGER.debug("Did not find EmptyBlock");
        return null;
      }
    } catch (EntityNotFound e) {
      LOGGER.error("Could not create Ladder");
      return null;
    }
    if (this.gameStore.getLadder(coordinates) != null) return this.gameStore.getLadder(coordinates);
    Entity entity = entityFactory.createLadder(coordinates);
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Projectile createProjectile(Coordinates coordinates, Vector2 velocity)
      throws ChunkNotFound, BodyNotFound {
    Projectile projectile = entityFactory.createProjectile(coordinates);
    this.gameStore.addEntity(projectile);
    projectile.setBodyVelocity(velocity);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            projectile.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    projectile.setEntityController(
        entityControllerFactory.createProjectileController(projectile, coordinates, 15));
    activeEntityManager.registerActiveEntity(user.getUserID(), projectile.getUuid());
    return projectile;
  }

  public Orb createOrb(Coordinates coordinates) throws ChunkNotFound {
    Orb orb = entityFactory.createOrb(coordinates);
    orb.setEntityController(entityControllerFactory.createOrbController(orb));
    this.gameStore.addEntity(orb);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            orb.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return orb;
  }

  public void triggerCreateTurret(Entity entity, Coordinates coordinates) {
    this.eventService.queuePostUpdateEvent(
        EventTypeFactory.createTurretEventType(entity.getUuid(), coordinates));
  }

  public Turret createTurret(Entity entity, Coordinates coordinates) throws ChunkNotFound {

    try {
      if (!(this.gameStore.getBlock(coordinates) instanceof EmptyBlock)) {
        LOGGER.debug("Did not find EmptyBlock");
        return null;
      }
    } catch (EntityNotFound e) {
      LOGGER.error(e);
      return null;
    }

    if (this.gameStore.getTurret(coordinates) != null) return this.gameStore.getTurret(coordinates);

    synchronized (entity.getBag()) {
      int orbIndex;
      try {
        orbIndex = entity.getBag().getClassIndex(OrbInventoryItem.class);
      } catch (ItemNotFoundException e) {
        return null;
      }
      entity.getBag().removeItem(orbIndex);
    }

    Turret turret = entityFactory.createTurret(coordinates);
    this.gameStore.addEntity(turret);
    CreateEntityOutgoingEventType createEntityOutgoingEvent =
        EventTypeFactory.createCreateEntityOutgoingEvent(
            turret.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    turret.setEntityController(entityControllerFactory.createTurretController(turret));
    activeEntityManager.registerActiveEntity(user.getUserID(), turret.getUuid());
    return turret;
  }

  public void moveEntity(UUID uuid, Coordinates coordinates) throws EntityNotFound {
    Entity entity = this.gameStore.getEntity(uuid);
    Coordinates preCoordinates = entity.coordinates;
    entity.coordinates = coordinates;
    this.eventService.fireEvent(
        EventTypeFactory.createUpdateEntityOutgoingEvent(
            coordinates, new ChunkRange(preCoordinates), uuid));
  }

  public void updateEntityAttribute(UUID uuid, Attribute attribute) throws EntityNotFound {
    Entity entity = this.gameStore.getEntity(uuid);
    Coordinates preCoordinates = entity.coordinates;
    AbstractEntityEventType entityAttributeEvent = entity.updateAttribute(attribute);
    eventService.fireEvent(entityAttributeEvent);
    this.eventService.fireEvent(
        EventTypeFactory.createUpdateEntityOutgoingEvent(
            attribute, new ChunkRange(preCoordinates), uuid));
  }

  public void placeBlock(Entity entity, Direction direction, Class blockClass)
      throws EntityNotFound {
    Block removeBlock = null;
    if (direction == Direction.LEFT) {
      removeBlock = this.gameStore.getBlock(entity.getCenter().getLeft());
    } else if (direction == Direction.RIGHT) {
      removeBlock = this.gameStore.getBlock(entity.getCenter().getRight());
    } else if (direction == Direction.UP) {
      removeBlock = this.gameStore.getBlock(entity.getCenter().getUp());
    } else if (direction == Direction.DOWN) {
      removeBlock = this.gameStore.getBlock(entity.getCenter().getDown());
    }
    if (removeBlock == null) throw new EntityNotFound("Block to remove not found in direction.");
    if (removeBlock.getClass() == blockClass) return;

    Block replacementBlock;
    if (blockClass == SkyBlock.class) {
      replacementBlock = blockFactory.createSky(removeBlock.coordinates);
    } else if (blockClass == DirtBlock.class) {
      replacementBlock = blockFactory.createDirt(removeBlock.coordinates);
    } else {
      return;
    }
    this.replaceBlock(removeBlock, replacementBlock);
  }

  public void replaceBlock(Block target, Block replacementBlock) {
    Ladder removeLadder = this.gameStore.getLadder(target.coordinates);
    if (removeLadder != null) {
      this.removeEntity(removeLadder.getUuid());
    }
    Turret removeTurret = this.gameStore.getTurret(target.coordinates);
    if (removeTurret != null) {
      this.removeEntity(removeTurret.getUuid());
    }
    // put this into a post update event
    this.eventService.queuePostUpdateEvent(
        EventTypeFactory.createReplaceEntityEvent(
            target.getUuid(), replacementBlock, false, new ChunkRange(target.coordinates)));
    this.eventService.fireEvent(
        EventTypeFactory.createReplaceBlockOutgoingEvent(
            target.getUuid(), replacementBlock, new ChunkRange(target.coordinates)));
  }

  public Entity triggerReplaceEntity(UUID target, Entity replacementEntity)
      throws EntityNotFound, ChunkNotFound, BodyNotFound, DestroyBodyException {
    return triggerReplaceEntity(target, replacementEntity, false);
  }

  public Entity triggerReplaceEntity(UUID target, Entity replacementEntity, Boolean swapVelocity)
      throws EntityNotFound, ChunkNotFound, BodyNotFound, DestroyBodyException {
    Vector2 velocity = null;
    Entity removeEntity = this.gameStore.getEntity(target);
    if (swapVelocity) {
      velocity = removeEntity.getBodyVelocity();
    }
    this.gameStore.removeEntity(removeEntity.getUuid());
    this.gameStore.addEntity(replacementEntity);

    if (swapVelocity) {
      replacementEntity.setBodyVelocity(velocity);
    }
    return replacementEntity;
  }

  public void createAI(UUID target) {
    this.eventService.queuePostUpdateEvent(
        EventTypeFactory.createAIEntityEventType(new Coordinates(0, 2), target));
  }
}
