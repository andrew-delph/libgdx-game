package infra.networking.consumer;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.chunk.Chunk;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.chunk.ChunkSubscriptionService;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntitySerializationConverter;
import infra.generation.ChunkGenerationManager;
import infra.networking.ConnectionStore;
import infra.networking.NetworkObjects;
import infra.networking.events.*;
import infra.networking.server.ServerNetworkHandle;

import java.util.List;
import java.util.UUID;

public class ServerEventConsumer extends NetworkConsumer {
  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameStore gameStore;
  @Inject EventFactory eventFactory;
  @Inject ChunkGenerationManager chunkGenerationManager;
  @Inject ChunkFactory chunkFactory;
  @Inject
    ConnectionStore connectionStore;

  public void init() {
    super.init();
    this.eventService.addListener(
        SubscriptionIncomingEvent.type,
        event -> {
          SubscriptionIncomingEvent realEvent = (SubscriptionIncomingEvent) event;

          chunkSubscriptionService.registerSubscription(
              realEvent.getUser(), realEvent.getChunkRangeList());

          for (ChunkRange chunkRange : realEvent.getChunkRangeList()) {
            System.out.println("sub: " + chunkRange);

            Chunk chunk = gameStore.getChunk(chunkRange);

            if (chunk == null) {
              System.out.println("does not exist " + chunkRange);
              this.gameStore.addChunk(this.chunkFactory.create(chunkRange));
              chunk = gameStore.getChunk(chunkRange);
            }

            for (Entity entity : chunk.getEntityList()) {
              serverNetworkHandle.send(
                  realEvent.getUser(),
                  eventFactory
                      .createCreateEntityOutgoingEvent(
                          entity.toNetworkData(), new ChunkRange(entity.coordinates))
                      .toNetworkEvent());
            }
          }
        });
    this.eventService.addListener(
        CreateEntityIncomingEvent.type,
        event -> {
          CreateEntityIncomingEvent realEvent = (CreateEntityIncomingEvent) event;
          Entity entity =
              gameController.createEntity(
                  entitySerializationConverter.createEntity(realEvent.getData()));
          System.out.println("created: "+entity.uuid);
          chunkGenerationManager.registerActiveEntity(
              entity, UUID.fromString(realEvent.networkEvent.getUser()));
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
          List<UUID> uuidList =
              chunkSubscriptionService.getSubscriptions(realEvent.getChunkRange());
          for (UUID uuid : uuidList) {
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
    this.eventService.addListener(
        DisconnectionEvent.type,
        event -> {
          DisconnectionEvent realEvent = (DisconnectionEvent) event;
            connectionStore.removeConnection(realEvent.getUuid());
            for (UUID uuid : chunkGenerationManager.getOwnerUuidList(realEvent.getUuid())) {
              Entity entity = this.gameStore.getEntity(uuid);
              this.gameStore.removeEntity(uuid);

              RemoveEntityOutgoingEvent removeEntityOutgoingEvent = eventFactory.createRemoveEntityOutgoingEvent(entity.toNetworkData(), new ChunkRange(entity.coordinates));
              for (UUID subscriptionUuid : chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
                  serverNetworkHandle.send(subscriptionUuid, removeEntityOutgoingEvent.toNetworkEvent());
              }
          }
        });
  }
}
