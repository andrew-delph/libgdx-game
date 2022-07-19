package core.networking.events.consumer.client;

import com.google.inject.Inject;
import core.common.events.EventConsumer;
import core.common.events.EventService;
import core.common.events.types.CreateAIEntityEventType;
import core.common.events.types.CreateTurretEventType;
import core.common.events.types.ItemActionEventType;
import core.networking.events.consumer.client.incoming.AuthenticationIncomingConsumerClient;
import core.networking.events.consumer.client.incoming.ChunkSwapIncomingConsumerClient;
import core.networking.events.consumer.client.incoming.CreateEntityIncomingConsumerClient;
import core.networking.events.consumer.client.incoming.RemoveEntityIncomingConsumerClient;
import core.networking.events.consumer.client.incoming.ReplaceBlockIncomingConsumerClient;
import core.networking.events.consumer.client.incoming.UpdateEntityIncomingConsumerClient;
import core.networking.events.consumer.client.outgoing.CreateAIEntityConsumerClient;
import core.networking.events.consumer.client.outgoing.CreateEntityOutgoingConsumerClient;
import core.networking.events.consumer.client.outgoing.HandshakeOutgoingConsumerClient;
import core.networking.events.consumer.client.outgoing.ItemActionOutgoingConsumerClient;
import core.networking.events.consumer.client.outgoing.ReplaceBlockOutgoingConsumerClient;
import core.networking.events.consumer.client.outgoing.UpdateEntityOutgoingConsumerClient;
import core.networking.events.types.NetworkEventTypeEnum;
import core.networking.events.consumer.client.incoming.HandshakeIncomingConsumerClient;
import core.networking.events.consumer.client.incoming.PingRequestIncomingConsumerClient;
import core.networking.events.consumer.client.incoming.PingResponseIncomingConsumerClient;
import core.networking.events.consumer.client.outgoing.CreateTurretOutgoingConsumerClient;
import core.networking.events.consumer.client.outgoing.PingRequestOutgoingConsumerClient;
import core.networking.events.consumer.client.outgoing.RemoveEntityOutgoingConsumerClient;
import core.networking.events.types.incoming.CreateEntityIncomingEventType;
import core.networking.events.types.incoming.RemoveEntityIncomingEventType;
import core.networking.events.types.incoming.ReplaceBlockIncomingEventType;
import core.networking.events.types.incoming.UpdateEntityIncomingEventType;
import core.networking.events.types.outgoing.CreateEntityOutgoingEventType;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import core.networking.events.types.outgoing.UpdateEntityOutgoingEventType;

public class ClientEventConsumer extends EventConsumer {
  @Inject EventService eventService;

  // CONSUMER IMPORT
  @Inject
  CreateEntityIncomingConsumerClient createEntityIncomingConsumer;
  @Inject
  UpdateEntityIncomingConsumerClient updateEntityIncomingConsumer;
  @Inject
  RemoveEntityIncomingConsumerClient removeEntityIncomingConsumer;
  @Inject
  ReplaceBlockIncomingConsumerClient replaceBlockIncomingConsumer;
  @Inject
  CreateEntityOutgoingConsumerClient createEntityOutgoingConsumer;
  @Inject
  UpdateEntityOutgoingConsumerClient updateEntityOutgoingConsumer;
  @Inject
  ReplaceBlockOutgoingConsumerClient replaceBlockOutgoingConsumer;
  @Inject
  CreateAIEntityConsumerClient createAIEntityConsumerClient;
  @Inject HandshakeIncomingConsumerClient handshakeIncomingConsumerClient;
  @Inject
  HandshakeOutgoingConsumerClient handshakeOutgoingConsumerClient;
  @Inject RemoveEntityOutgoingConsumerClient removeEntityOutgoingConsumerClient;
  @Inject
  ChunkSwapIncomingConsumerClient chunkSwapIncomingConsumerClient;
  @Inject
  AuthenticationIncomingConsumerClient authenticationIncomingConsumerClient;
  @Inject PingRequestOutgoingConsumerClient pingRequestOutgoingConsumerClient;
  @Inject PingRequestIncomingConsumerClient pingRequestIncomingConsumerClient;
  @Inject PingResponseIncomingConsumerClient pingResponseIncomingConsumerClient;
  @Inject CreateTurretOutgoingConsumerClient createTurretOutgoingConsumerClient;
  @Inject
  ItemActionOutgoingConsumerClient itemActionOutgoingConsumerClient;

  @Inject
  protected ClientEventConsumer() {}

  public void init() {
    super.init();
    this.eventService.addListener(CreateEntityIncomingEventType.type, createEntityIncomingConsumer);
    this.eventService.addListener(UpdateEntityIncomingEventType.type, updateEntityIncomingConsumer);
    this.eventService.addPostUpdateListener(
        ReplaceBlockIncomingEventType.type, replaceBlockIncomingConsumer);
    this.eventService.addListener(CreateEntityOutgoingEventType.type, createEntityOutgoingConsumer);
    this.eventService.addListener(UpdateEntityOutgoingEventType.type, updateEntityOutgoingConsumer);
    this.eventService.addListener(ReplaceBlockOutgoingEventType.type, replaceBlockOutgoingConsumer);
    this.eventService.addPostUpdateListener(
        CreateAIEntityEventType.type, createAIEntityConsumerClient);
    this.eventService.addListener(
        NetworkEventTypeEnum.HANDSHAKE_INCOMING, handshakeIncomingConsumerClient);
    this.eventService.addListener(
        NetworkEventTypeEnum.HANDSHAKE_OUTGOING, handshakeOutgoingConsumerClient);
    this.eventService.addListener(
        NetworkEventTypeEnum.REMOVE_ENTITY_OUTGOING, removeEntityOutgoingConsumerClient);
    this.eventService.addPostUpdateListener(
        RemoveEntityIncomingEventType.type, removeEntityIncomingConsumer);
    this.eventService.addPostUpdateListener(
        NetworkEventTypeEnum.CHUNK_SWAP_INCOMING, chunkSwapIncomingConsumerClient);
    this.eventService.addListener(
        NetworkEventTypeEnum.AUTH_INCOMING, authenticationIncomingConsumerClient);
    this.eventService.addListener(
        NetworkEventTypeEnum.PING_REQUEST_OUTGOING, pingRequestOutgoingConsumerClient);
    this.eventService.addListener(
        NetworkEventTypeEnum.PING_REQUEST_INCOMING, pingRequestIncomingConsumerClient);
    this.eventService.addListener(
        NetworkEventTypeEnum.PING_RESPONSE_INCOMING, pingResponseIncomingConsumerClient);
    this.eventService.addPostUpdateListener(
        CreateTurretEventType.type, createTurretOutgoingConsumerClient);

    this.eventService.addPostUpdateListener(
        ItemActionEventType.type, itemActionOutgoingConsumerClient);
  }
}
