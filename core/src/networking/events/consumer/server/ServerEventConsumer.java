package networking.events.consumer.server;

import com.google.inject.Inject;
import common.events.EventConsumer;
import common.events.EventService;
import common.events.types.CreateAIEntityEventType;
import common.events.types.CreateTurretEventType;
import common.events.types.ItemActionEventType;
import networking.events.consumer.server.incoming.AuthenticationIncomingConsumerServer;
import networking.events.consumer.server.incoming.CreateAIEntityConsumerServer;
import networking.events.consumer.server.incoming.CreateEntityIncomingConsumerServer;
import networking.events.consumer.server.incoming.CreateTurretIncomingConsumerServer;
import networking.events.consumer.server.incoming.DisconnectionIncomingConsumerServer;
import networking.events.consumer.server.incoming.HandshakeIncomingConsumerServer;
import networking.events.consumer.server.incoming.ItemActionIncomingConsumerServer;
import networking.events.consumer.server.incoming.PingRequestIncomingConsumerServer;
import networking.events.consumer.server.incoming.PingResponseIncomingConsumerServer;
import networking.events.consumer.server.incoming.RemoveEntityIncomingConsumerServer;
import networking.events.consumer.server.incoming.ReplaceBlockIncomingConsumerServer;
import networking.events.consumer.server.incoming.SubscriptionIncomingConsumerServer;
import networking.events.consumer.server.incoming.UpdateEntityIncomingConsumerServer;
import networking.events.consumer.server.outgoing.ChunkSwapOutgoingConsumerServer;
import networking.events.consumer.server.outgoing.CreateEntityOutgoingConsumerServer;
import networking.events.consumer.server.outgoing.PingRequestOutgoingConsumerServer;
import networking.events.consumer.server.outgoing.RemoveEntityOutgoingConsumerServer;
import networking.events.consumer.server.outgoing.ReplaceBlockOutgoingConsumerServer;
import networking.events.consumer.server.outgoing.UpdateEntityOutgoingConsumerServer;
import networking.events.types.NetworkEventTypeEnum;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.incoming.DisconnectionIncomingEventType;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.incoming.SubscriptionIncomingEventType;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;

public class ServerEventConsumer extends EventConsumer {
  @Inject EventService eventService;

  @Inject SubscriptionIncomingConsumerServer subscriptionIncomingConsumerServer;
  @Inject DisconnectionIncomingConsumerServer disconnectionIncomingConsumerServer;
  @Inject CreateEntityIncomingConsumerServer createEntityIncomingConsumerServer;
  @Inject UpdateEntityIncomingConsumerServer updateEntityIncomingConsumerServer;
  @Inject ReplaceBlockIncomingConsumerServer replaceBlockIncomingConsumerServer;
  @Inject CreateEntityOutgoingConsumerServer createEntityOutgoingConsumerServer;
  @Inject UpdateEntityOutgoingConsumerServer updateEntityOutgoingConsumerServer;
  @Inject ReplaceBlockOutgoingConsumerServer replaceBlockOutgoingConsumerServer;
  @Inject CreateAIEntityConsumerServer createAIEntityConsumerServer;
  @Inject HandshakeIncomingConsumerServer handshakeIncomingConsumerServer;
  @Inject RemoveEntityIncomingConsumerServer removeEntityIncomingConsumerServer;
  @Inject RemoveEntityOutgoingConsumerServer removeEntityOutgoingConsumerServer;
  @Inject ChunkSwapOutgoingConsumerServer chunkSwapOutgoingConsumerServer;
  @Inject AuthenticationIncomingConsumerServer authenticationIncomingConsumerServer;
  @Inject PingRequestOutgoingConsumerServer pingRequestOutgoingConsumerServer;
  @Inject PingRequestIncomingConsumerServer pingRequestIncomingConsumerServer;
  @Inject PingResponseIncomingConsumerServer pingResponseIncomingConsumerServer;
  @Inject CreateTurretIncomingConsumerServer createTurretIncomingConsumerServer;
  @Inject ItemActionIncomingConsumerServer itemActionIncomingConsumerServer;

  public void init() {
    super.init();
    this.eventService.addListener(
        SubscriptionIncomingEventType.type, subscriptionIncomingConsumerServer);
    this.eventService.addListener(
        DisconnectionIncomingEventType.type, disconnectionIncomingConsumerServer);
    this.eventService.addListener(
        CreateEntityIncomingEventType.type, createEntityIncomingConsumerServer);
    this.eventService.addListener(
        UpdateEntityIncomingEventType.type, updateEntityIncomingConsumerServer);
    this.eventService.addPostUpdateListener(
        ReplaceBlockIncomingEventType.type, replaceBlockIncomingConsumerServer);
    this.eventService.addListener(
        ReplaceBlockOutgoingEventType.type, replaceBlockOutgoingConsumerServer);
    this.eventService.addListener(
        CreateEntityOutgoingEventType.type, createEntityOutgoingConsumerServer);
    this.eventService.addListener(
        UpdateEntityOutgoingEventType.type, updateEntityOutgoingConsumerServer);
    this.eventService.addPostUpdateListener(
        CreateAIEntityEventType.type, createAIEntityConsumerServer);
    this.eventService.addListener(
        NetworkEventTypeEnum.HANDSHAKE_INCOMING, handshakeIncomingConsumerServer);
    this.eventService.addPostUpdateListener(
        NetworkEventTypeEnum.REMOVE_ENTITY_INCOMING, removeEntityIncomingConsumerServer);
    this.eventService.addListener(
        NetworkEventTypeEnum.REMOVE_ENTITY_OUTGOING, removeEntityOutgoingConsumerServer);
    this.eventService.addListener(
        NetworkEventTypeEnum.CHUNK_SWAP_OUTGOING, chunkSwapOutgoingConsumerServer);
    this.eventService.addListener(
        NetworkEventTypeEnum.AUTH_INCOMING, authenticationIncomingConsumerServer);
    this.eventService.addListener(
        NetworkEventTypeEnum.PING_REQUEST_OUTGOING, pingRequestOutgoingConsumerServer);
    this.eventService.addListener(
        NetworkEventTypeEnum.PING_REQUEST_INCOMING, pingRequestIncomingConsumerServer);
    this.eventService.addListener(
        NetworkEventTypeEnum.PING_RESPONSE_INCOMING, pingResponseIncomingConsumerServer);
    this.eventService.addPostUpdateListener(
        CreateTurretEventType.type, createTurretIncomingConsumerServer);
    this.eventService.addPostUpdateListener(
        ItemActionEventType.type, itemActionIncomingConsumerServer);
  }
}
