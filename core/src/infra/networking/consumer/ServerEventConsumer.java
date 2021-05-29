package infra.networking.consumer;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.chunk.Chunk;
import infra.chunk.ChunkRange;
import infra.chunk.ChunkSubscriptionService;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntitySerializationConverter;
import infra.networking.NetworkObjects;
import infra.networking.events.*;
import infra.networking.server.ServerNetworkHandle;
import org.checkerframework.checker.units.qual.C;

import java.util.UUID;

public class ServerEventConsumer extends NetworkConsumer {
  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameStore gameStore;
  @Inject EventFactory eventFactory;

  public void init() {
    super.init();
    this.eventService.addListener(
        SubscriptionIncomingEvent.type,
        event -> {
          SubscriptionIncomingEvent realEvent = (SubscriptionIncomingEvent) event;
          chunkSubscriptionService.registerSubscription(
              realEvent.getUser(), realEvent.getChunkRangeList());

          for (ChunkRange chunkRange :
              chunkSubscriptionService.getUserChunkRangeSubscriptions(realEvent.getUser())) {

            Chunk chunk = gameStore.getChunk(chunkRange);

            for (Entity entity : chunk.getEntityList()) {
              serverNetworkHandle.send(
                  realEvent.getUser(),
                  eventFactory
                      .createCreateEntityOutgoingEvent(entity.toNetworkData(), new ChunkRange(entity.coordinates))
                      .toNetworkEvent());
            }
          }
        });
    this.eventService.addListener(
        CreateEntityIncomingEvent.type,
        event -> {
          CreateEntityIncomingEvent realEvent = (CreateEntityIncomingEvent) event;
          gameController.createEntity(
              entitySerializationConverter.createEntity(realEvent.getData()));
        });
    this.eventService.addListener(
        UpdateEntityIncomingEvent.type,
        event -> {
          UpdateEntityIncomingEvent realEvent = (UpdateEntityIncomingEvent) event;
          Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
          gameController.moveEntity(entity.uuid, entity.coordinates);
        });
    this.eventService.addListener(
        CreateEntityOutgoingEvent.type,
        event -> {
          CreateEntityOutgoingEvent realEvent = (CreateEntityOutgoingEvent) event;
          NetworkObjects.NetworkEvent networkEvent = realEvent.toNetworkEvent();
          for (UUID uuid : chunkSubscriptionService.getSubscriptions(realEvent.getChunkRange())) {
            serverNetworkHandle.send(uuid, networkEvent);
          }
        });
    this.eventService.addListener(
        UpdateEntityOutgoingEvent.type,
        event -> {
          UpdateEntityOutgoingEvent realEvent = (UpdateEntityOutgoingEvent) event;
          NetworkObjects.NetworkEvent networkEvent = realEvent.toNetworkEvent();
          for (UUID uuid : chunkSubscriptionService.getSubscriptions(realEvent.getChunkRange())) {
            serverNetworkHandle.send(uuid, networkEvent);
          }
        });
  }
}
