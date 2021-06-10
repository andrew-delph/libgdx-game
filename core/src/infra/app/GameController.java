package infra.app;

import com.google.inject.Inject;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.Direction;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.block.Block;
import infra.entity.block.BlockFactory;
import infra.entity.block.SkyBlock;
import infra.networking.events.CreateEntityOutgoingEvent;
import infra.networking.events.EventFactory;

import java.util.UUID;

public class GameController {
  @Inject GameStore gameStore;

  @Inject EntityFactory entityFactory;

  @Inject EventService eventService;

  @Inject EventFactory eventFactory;

  @Inject BlockFactory blockFactory;

  public Entity createEntity(Entity entity) {
    this.gameStore.addEntity(entity);

    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(entity.coordinates));

    this.eventService.fireEvent(createEntityOutgoingEvent);

    return entity;
  }

  public Entity createBlock(Coordinates coordinates) {
    Entity entity = blockFactory.create();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity createSkyBlock(Coordinates coordinates) {
    Entity entity = blockFactory.createSky();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity createDirtBlock(Coordinates coordinates) {
    Entity entity = blockFactory.createDirt();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity createStoneBlock(Coordinates coordinates) {
    Entity entity = blockFactory.createStone();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates));
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity triggerCreateEntity(Entity entity) {
    this.gameStore.addEntity(entity);
    return entity;
  }

  public void moveEntity(UUID uuid, Coordinates coordinates) {
    Entity entity = this.gameStore.getEntity(uuid);
    entity.coordinates = coordinates;
    this.eventService.fireEvent(
        eventFactory.createUpdateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates)));
  }

  public void triggerMoveEntity(Entity entity, Coordinates coordinates) {
    entity.coordinates = coordinates;
    this.eventService.fireEvent(
        eventFactory.createUpdateEntityOutgoingEvent(
            entity.toNetworkData(), new ChunkRange(coordinates)));
  }

  public void removeEntity(UUID uuid) {
    this.gameStore.removeEntity(uuid);
  }

  public void dig(Entity entity, Direction direction) {
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
    if (removeBlock == null) return;
    // put this into a post update event
    this.eventService.queuePostUpdateEvent(
        this.eventFactory.createReplaceBlockEvent(removeBlock.uuid, SkyBlock.class.getName()));
    this.eventService.fireEvent(this.eventFactory.createReplaceBlockOutgoingEvent(removeBlock.uuid,SkyBlock.class.getName(), new ChunkRange(removeBlock.coordinates)));
  }

  public Entity replaceBlock(UUID target, String replacementBlockType) {
    Block removeBlock = (Block) this.gameStore.removeEntity(target);
    if (removeBlock == null) return null;
    Block newBlock = blockFactory.createSky();
    newBlock.coordinates = removeBlock.coordinates;
    this.gameStore.addEntity(newBlock);
    return newBlock;
  }
}
