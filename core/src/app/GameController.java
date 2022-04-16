package app;

import chunk.ChunkRange;
import chunk.world.exceptions.BodyNotFound;
import chunk.world.exceptions.DestroyBodyException;
import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import common.Coordinates;
import common.Direction;
import common.GameStore;
import common.events.EventService;
import common.exceptions.ChunkNotFound;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.EntityFactory;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.DirtBlock;
import entity.block.EmptyBlock;
import entity.block.SkyBlock;
import entity.misc.Ladder;
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

  public void moveEntity(UUID uuid, Coordinates coordinates) throws EntityNotFound {
    Entity entity = this.gameStore.getEntity(uuid);
    Coordinates preCoordinates = entity.coordinates;
    entity.coordinates = coordinates;
    this.eventService.fireEvent(
        EventTypeFactory.createUpdateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(preCoordinates)));
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
