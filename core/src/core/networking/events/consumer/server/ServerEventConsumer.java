package core.networking.events.consumer.server;

import com.google.inject.Inject;
import core.common.events.EventConsumer;
import core.common.events.EventService;
import core.common.events.types.CreateAIEntityEventType;
import core.common.events.types.CreateTurretEventType;
import core.common.events.types.ItemActionEventType;
import core.networking.events.consumer.server.incoming.AuthenticationIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.CreateAIEntityConsumerServer;
import core.networking.events.consumer.server.incoming.CreateEntityIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.CreateTurretIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.DisconnectionIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.HandshakeIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.ItemActionIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.PingRequestIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.PingResponseIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.RemoveEntityIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.ReplaceBlockIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.SubscriptionIncomingConsumerServer;
import core.networking.events.consumer.server.incoming.UpdateEntityIncomingConsumerServer;
import core.networking.events.consumer.server.outgoing.ChunkSwapOutgoingConsumerServer;
import core.networking.events.consumer.server.outgoing.CreateEntityOutgoingConsumerServer;
import core.networking.events.consumer.server.outgoing.PingRequestOutgoingConsumerServer;
import core.networking.events.consumer.server.outgoing.RemoveEntityOutgoingConsumerServer;
import core.networking.events.consumer.server.outgoing.ReplaceBlockOutgoingConsumerServer;
import core.networking.events.consumer.server.outgoing.UpdateEntityOutgoingConsumerServer;
import core.networking.events.types.NetworkEventTypeEnum;
import core.networking.events.types.incoming.CreateEntityIncomingEventType;
import core.networking.events.types.incoming.DisconnectionIncomingEventType;
import core.networking.events.types.incoming.ReplaceBlockIncomingEventType;
import core.networking.events.types.incoming.SubscriptionIncomingEventType;
import core.networking.events.types.incoming.UpdateEntityIncomingEventType;
import core.networking.events.types.outgoing.CreateEntityOutgoingEventType;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import core.networking.events.types.outgoing.UpdateEntityOutgoingEventType;

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
