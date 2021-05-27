package infra.app;

import com.google.inject.Inject;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.block.BlockFactory;
import infra.networking.events.CreateEntityOutgoingEvent;
import infra.networking.events.EventFactory;

public class GameController {
  @Inject GameStore gameStore;

  @Inject EntityFactory entityFactory;

  @Inject EventService eventService;

  @Inject EventFactory eventFactory;

  @Inject BlockFactory blockFactory;

  public Entity createEntity(Coordinates coordinates) {
    Entity entity = entityFactory.createEntity();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);

    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(entity.toNetworkData());

    this.eventService.fireEvent(createEntityOutgoingEvent);

    return entity;
  }

  public Entity createBlock(Coordinates coordinates) {
    Entity entity = blockFactory.create();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(entity.toNetworkData());
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity createSkyBlock(Coordinates coordinates) {
    Entity entity = blockFactory.createSky();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(entity.toNetworkData());
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity createDirtBlock(Coordinates coordinates) {
    Entity entity = blockFactory.createDirt();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(entity.toNetworkData());
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public Entity createStoneBlock(Coordinates coordinates) {
    Entity entity = blockFactory.createStone();
    entity.coordinates = coordinates;
    this.gameStore.addEntity(entity);
    CreateEntityOutgoingEvent createEntityOutgoingEvent =
        eventFactory.createCreateEntityOutgoingEvent(entity.toNetworkData());
    this.eventService.fireEvent(createEntityOutgoingEvent);
    return entity;
  }

  public void triggerCreateEntity(Entity entity) {
    this.gameStore.addEntity(entity);
  }

  public void moveEntity(Entity entity, Coordinates coordinates) {
    entity.coordinates = coordinates;
    this.eventService.fireEvent(
        eventFactory.createUpdateEntityOutgoingEvent(entity.toNetworkData()));
  }

  public void triggerMoveEntity(Entity entity, Coordinates coordinates) {
    entity.coordinates = coordinates;
    this.eventService.fireEvent(
        eventFactory.createUpdateEntityOutgoingEvent(entity.toNetworkData()));
  }
}
