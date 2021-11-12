package networking.consumer;

import app.GameController;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.GameStore;
import common.events.Event;
import common.events.EventConsumer;
import common.events.EventService;
import entity.Entity;
import entity.EntitySerializationConverter;
import entity.block.Block;
import generation.ChunkGenerationManager;
import networking.ConnectionStore;
import networking.NetworkObjects;
import networking.events.*;
import networking.server.ServerNetworkHandle;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServerEventConsumer extends EventConsumer {
  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject GameStore gameStore;
  @Inject EventFactory eventFactory;
  @Inject ChunkGenerationManager chunkGenerationManager;
  @Inject ChunkFactory chunkFactory;
  @Inject ConnectionStore connectionStore;

  public void init() {
    super.init();

    Consumer<Event> subscriptionIncoming =
        event -> {
          SubscriptionIncomingEvent realEvent = (SubscriptionIncomingEvent) event;
          List<ChunkRange> userChunkRangeList =
              chunkSubscriptionService.getUserChunkRangeSubscriptions(realEvent.getUser());
          Predicate<ChunkRange> doesNotContain = (userChunkRangeList::contains);
          doesNotContain = doesNotContain.negate();
          List<ChunkRange> newChunkRangeList =
              realEvent.getChunkRangeList().stream()
                  .distinct()
                  .filter(doesNotContain)
                  .collect(Collectors.toList());
          chunkSubscriptionService.registerSubscription(
              realEvent.getUser(), realEvent.getChunkRangeList());
          for (ChunkRange chunkRange : newChunkRangeList) {
            Chunk chunk = gameStore.getChunk(chunkRange);
            if (chunk == null) {
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
        };

      Consumer<Event> disconnectEvent =
              event -> {
                  DisconnectionEvent realEvent = (DisconnectionEvent) event;
                  connectionStore.removeConnection(realEvent.getUuid());
                  for (UUID ownersEntityUuid :
                          chunkGenerationManager.getOwnerUuidList(realEvent.getUuid())) {
                      Entity entity = this.gameStore.getEntity(ownersEntityUuid);
                      this.eventService.queuePostUpdateEvent(
                              eventFactory.createRemoveEntityEvent(ownersEntityUuid));

                      RemoveEntityOutgoingEvent removeEntityOutgoingEvent =
                              eventFactory.createRemoveEntityOutgoingEvent(
                                      entity.toNetworkData(), new ChunkRange(entity.coordinates));
                      for (UUID subscriptionUuid :
                              chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
                          serverNetworkHandle.send(
                                  subscriptionUuid, removeEntityOutgoingEvent.toNetworkEvent());
                      }
                  }
              };


    this.eventService.addListener(SubscriptionIncomingEvent.type, subscriptionIncoming);
    this.eventService.addListener(DisconnectionEvent.type, disconnectEvent);

    Consumer<Event> createEntityIncoming =
        event -> {
          CreateEntityIncomingEvent realEvent = (CreateEntityIncomingEvent) event;
          Entity entity =
              gameController.triggerCreateEntity(
                  entitySerializationConverter.createEntity(realEvent.getData()));
          chunkGenerationManager.registerActiveEntity(
              entity, UUID.fromString(realEvent.networkEvent.getUser()));

          for (UUID uuid :
              chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
            if (uuid.equals(realEvent.getUser())) continue;
            serverNetworkHandle.send(uuid, realEvent.networkEvent);
          }
        };

    Consumer<Event> updateEntityIncoming =
        event -> {
          UpdateEntityIncomingEvent realEvent = (UpdateEntityIncomingEvent) event;
          Entity entity = entitySerializationConverter.updateEntity(realEvent.getData());
          for (UUID uuid :
              chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
            if (uuid.equals(realEvent.getUser())) continue;
            serverNetworkHandle.send(uuid, realEvent.networkEvent);
          }
        };

    Consumer<Event> replaceBlockIncoming =
        event -> {
          ReplaceBlockIncomingEvent realEvent = (ReplaceBlockIncomingEvent) event;
          Entity placedEntity = this.gameStore.getEntity(realEvent.getTarget());
          ChunkRange chunkRange = new ChunkRange(placedEntity.coordinates);
          this.eventService.queuePostUpdateEvent(
              this.eventFactory.createReplaceBlockEvent(
                  realEvent.getTarget(),
                  (Block)
                      entitySerializationConverter.createEntity(
                          realEvent.getReplacementBlockData())));
          for (UUID uuid : chunkSubscriptionService.getSubscriptions(chunkRange)) {
            serverNetworkHandle.send(uuid, realEvent.networkEvent);
          }
        };

    this.eventService.addListener(CreateEntityIncomingEvent.type, createEntityIncoming);
    this.eventService.addListener(UpdateEntityIncomingEvent.type, updateEntityIncoming);
    this.eventService.addListener(ReplaceBlockIncomingEvent.type, replaceBlockIncoming);

    Consumer<Event> createEntityOutgoing =
        event -> {
          CreateEntityOutgoingEvent realEvent = (CreateEntityOutgoingEvent) event;
          NetworkObjects.NetworkEvent networkEvent = realEvent.toNetworkEvent();
          List<UUID> uuidList =
              chunkSubscriptionService.getSubscriptions(realEvent.getChunkRange());
          for (UUID uuid : uuidList) {
            serverNetworkHandle.send(uuid, networkEvent);
          }
        };

    Consumer<Event> updateEntityOutgoing =
        event -> {
          UpdateEntityOutgoingEvent realEvent = (UpdateEntityOutgoingEvent) event;
          NetworkObjects.NetworkEvent networkEvent = realEvent.toNetworkEvent();
          for (UUID uuid : chunkSubscriptionService.getSubscriptions(realEvent.getChunkRange())) {
            serverNetworkHandle.send(uuid, networkEvent);
          }
        };

    Consumer<Event> replaceBlockOutgoing =
        event -> {
          ReplaceBlockOutgoingEvent realEvent = (ReplaceBlockOutgoingEvent) event;
          this.eventService.queuePostUpdateEvent(event);
          for (UUID uuid : chunkSubscriptionService.getSubscriptions(realEvent.getChunkRange())) {
            serverNetworkHandle.send(uuid, realEvent.toNetworkEvent());
          }
        };

    this.eventService.addListener(CreateEntityOutgoingEvent.type, createEntityOutgoing);
    this.eventService.addListener(UpdateEntityOutgoingEvent.type, updateEntityOutgoing);
    this.eventService.addListener(ReplaceBlockOutgoingEvent.type, replaceBlockOutgoing);
  }
}
