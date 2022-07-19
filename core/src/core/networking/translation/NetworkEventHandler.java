package core.networking.translation;

import com.google.inject.Inject;
import core.app.user.UserID;
import core.common.events.EventConsumer;
import core.common.events.EventService;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.outgoing.SubscriptionOutgoingEventType;
import core.networking.ping.PingService;
import networking.NetworkObjects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkEventHandler extends EventConsumer {

  final Logger LOGGER = LogManager.getLogger();
  @Inject EventTypeFactory eventTypeFactory;
  @Inject EventService eventService;
  @Inject NetworkDataDeserializer networkDataDeserializer;
  @Inject PingService pingService;

  public NetworkEventHandler() {
    super();
  }

  public void handleNetworkEvent(NetworkObjects.NetworkEvent networkEvent) {
    try {
      Long delay;
      String event = networkEvent.getEvent();
      Long time = networkEvent.getTime();
      UserID receivedFrom;
      receivedFrom = UserID.createUserID(networkEvent.getUser());
      delay = pingService.calcDelay(receivedFrom, time);
      if (delay < 0) delay = 0L;
      if (event.equals(DataTranslationEnum.CREATE_ENTITY)) {
        eventService.fireEvent(
            delay, NetworkDataDeserializer.createCreateEntityIncomingEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.UPDATE_ENTITY)) {
        eventService.fireEvent(
            delay, NetworkDataDeserializer.createUpdateEntityIncomingEvent(networkEvent));
      } else if (event.equals(SubscriptionOutgoingEventType.type)) {
        eventService.fireEvent(eventTypeFactory.createSubscriptionIncomingEvent(networkEvent));
      } else if (event.equals(DataTranslationEnum.REMOVE_ENTITY)) {
        eventService.queuePostUpdateEvent(
            delay, NetworkDataDeserializer.createRemoveEntityIncomingEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.REPLACE_BLOCK)) {
        eventService.queuePostUpdateEvent(
            delay, networkDataDeserializer.createReplaceBlockIncomingEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.CREATE_AI)) {
        eventService.queuePostUpdateEvent(
            delay, NetworkDataDeserializer.createCreateAIEntityEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.HANDSHAKE)) {
        eventService.fireEvent(
            NetworkDataDeserializer.createHandshakeIncomingEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.CHUNK_SWAP)) {
        eventService.queuePostUpdateEvent(
            NetworkDataDeserializer.createChunkSwapIncomingEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.REQUEST_PING)) {
        eventService.fireEvent(
            NetworkDataDeserializer.createPingRequestIncomingEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.RESPONSE_PING)) {
        eventService.fireEvent(
            NetworkDataDeserializer.createPingResponseIncomingEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.CREATE_TURRET)) {
        eventService.queuePostUpdateEvent(
            delay, NetworkDataDeserializer.createCreateTurretEventType(networkEvent));
      } else if (event.equals(DataTranslationEnum.ITEM_ACTION)) {
        eventService.queuePostUpdateEvent(
            delay, NetworkDataDeserializer.createItemActionEventType(networkEvent));
      }
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }
}
