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
import infra.entity.block.DirtBlock;
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

  public void placeBlock(Entity entity, Direction direction, Class blockClass){
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

    Block replacementBlock;
    if (blockClass == SkyBlock.class){
      replacementBlock = blockFactory.createSky();
    }
    else if(blockClass == DirtBlock.class){
      replacementBlock = blockFactory.createDirt();
    }
    else {
      return;
    }
    // put this into a post update event
    this.eventService.queuePostUpdateEvent(
            this.eventFactory.createReplaceBlockEvent(removeBlock.uuid, replacementBlock));
    this.eventService.fireEvent(
            this.eventFactory.createReplaceBlockOutgoingEvent(
                    removeBlock.uuid, replacementBlock, new ChunkRange(removeBlock.coordinates)));
  }

  public Entity replaceBlock(UUID target, Block replacementBlock) {
    Block removeBlock = (Block) this.gameStore.removeEntity(target);
    if (removeBlock == null) return null;
    replacementBlock.coordinates = removeBlock.coordinates;
    this.gameStore.addEntity(replacementBlock);
    return replacementBlock;
  }
}
