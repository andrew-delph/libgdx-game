package networking.translation;

import com.google.inject.Inject;
import common.events.EventConsumer;
import common.events.EventService;
import networking.NetworkObjects;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.SubscriptionOutgoingEventType;

public class NetworkEventHandler extends EventConsumer {

    @Inject
    EventTypeFactory eventTypeFactory;
    @Inject
    EventService eventService;
    @Inject
    NetworkDataDeserializer networkDataDeserializer;

    public NetworkEventHandler() {
        super();
    }

    public void handleNetworkEvent(NetworkObjects.NetworkEvent networkEvent) {
        try {
            String event = networkEvent.getEvent();
            if (event.equals(DataTranslationEnum.CREATE_ENTITY)) {
                eventService.fireEvent(NetworkDataDeserializer.createCreateEntityIncomingEventType(networkEvent));
            } else if (event.equals(DataTranslationEnum.UPDATE_ENTITY)) {
                eventService.fireEvent(NetworkDataDeserializer.createUpdateEntityIncomingEvent(networkEvent));
            } else if (event.equals(SubscriptionOutgoingEventType.type)) {
                eventService.fireEvent(eventTypeFactory.createSubscriptionIncomingEvent(networkEvent));
            } else if (event.equals(DataTranslationEnum.REMOVE_ENTITY)) {
                eventService.queuePostUpdateEvent(NetworkDataDeserializer.createRemoveEntityIncomingEventType(networkEvent));
            } else if (event.equals(DataTranslationEnum.REPLACE_BLOCK)) {
                eventService.queuePostUpdateEvent(networkDataDeserializer.createReplaceBlockIncomingEventType(networkEvent));
            } else if (event.equals(DataTranslationEnum.CREATE_AI)) {
                eventService.queuePostUpdateEvent(NetworkDataDeserializer.createCreateAIEntityEventType(networkEvent));
            } else if (event.equals(DataTranslationEnum.HANDSHAKE)) {
                eventService.fireEvent(NetworkDataDeserializer.createHandshakeIncomingEventType(networkEvent));
            } else if (event.equals(DataTranslationEnum.CHUNK_SWAP)) {
                eventService.queuePostUpdateEvent(NetworkDataDeserializer.createChunkSwapIncomingEventType(networkEvent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
