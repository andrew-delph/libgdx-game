package infra.networking.consumer;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.chunk.ChunkSubscriptionService;
import infra.common.events.EventService;
import infra.entity.EntitySerializationConverter;
import infra.networking.NetworkObjects;
import infra.networking.events.*;
import infra.networking.server.ServerNetworkHandle;

import java.util.UUID;

public class ServerEventConsumer extends NetworkConsumer {
  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;

  public void init() {
    this.eventService.addListener(
        SubscriptionIncomingEvent.type,
        event -> {
          SubscriptionIncomingEvent realEvent = (SubscriptionIncomingEvent) event;
          chunkSubscriptionService.registerSubscription(
              realEvent.getUser(), realEvent.getChunkRangeList());
        });
    this.eventService.addListener(
        CreateEntityIncomingEvent.type,
        event -> {
          CreateEntityIncomingEvent realEvent = (CreateEntityIncomingEvent) event;
          gameController.triggerCreateEntity(
              entitySerializationConverter.createEntity(realEvent.getData()));
        });
    this.eventService.addListener(
        UpdateEntityIncomingEvent.type,
        event -> {
          UpdateEntityIncomingEvent realEvent = (UpdateEntityIncomingEvent) event;
          gameController.triggerCreateEntity(
              entitySerializationConverter.createEntity(realEvent.getData()));
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
