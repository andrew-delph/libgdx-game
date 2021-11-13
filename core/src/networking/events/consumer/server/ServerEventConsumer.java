package networking.events.consumer.server;

import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.EventConsumer;
import common.events.EventService;
import networking.events.consumer.server.incoming.*;
import networking.events.consumer.server.outgoing.CreateEntityOutgoingConsumerServer;
import networking.events.consumer.server.outgoing.ReplaceBlockOutgoingConsumerServer;
import networking.events.consumer.server.outgoing.UpdateEntityOutgoingConsumerServer;
import networking.events.types.incoming.*;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import networking.server.ServerNetworkHandle;

public class ServerEventConsumer extends EventConsumer {
  @Inject EventService eventService;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;

  @Inject SubscriptionIncomingConsumerServer subscriptionIncomingConsumerServer;
  @Inject DisconnectionIncomingConsumerServer disconnectionIncomingConsumerServer;
  @Inject CreateEntityIncomingConsumerServer createEntityIncomingConsumerServer;
  @Inject UpdateEntityIncomingServerConsumer updateEntityIncomingServerConsumer;
  @Inject ReplaceBlockIncomingConsumerServer replaceBlockIncomingConsumerServer;
  @Inject CreateEntityOutgoingConsumerServer createEntityOutgoingConsumerServer;
  @Inject UpdateEntityOutgoingConsumerServer updateEntityOutgoingConsumerServer;
  @Inject ReplaceBlockOutgoingConsumerServer replaceBlockOutgoingConsumerServer;

  public void init() {
    super.init();
    this.eventService.addListener(
        SubscriptionIncomingEventType.type, subscriptionIncomingConsumerServer);
    this.eventService.addListener(
        DisconnectionIncomingEventType.type, disconnectionIncomingConsumerServer);
    this.eventService.addListener(
        CreateEntityIncomingEventType.type, createEntityIncomingConsumerServer);
    this.eventService.addListener(
        UpdateEntityIncomingEventType.type, updateEntityIncomingServerConsumer);
    this.eventService.addListener(
        ReplaceBlockIncomingEventType.type, replaceBlockIncomingConsumerServer);
    this.eventService.addListener(
        CreateEntityOutgoingEventType.type, createEntityOutgoingConsumerServer);
    this.eventService.addListener(
        UpdateEntityOutgoingEventType.type, updateEntityOutgoingConsumerServer);
    this.eventService.addListener(
        ReplaceBlockOutgoingEventType.type, replaceBlockOutgoingConsumerServer);
  }
}
