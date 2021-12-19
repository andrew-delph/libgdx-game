package networking.events.consumer.client;

import com.google.inject.Inject;
import common.events.EventConsumer;
import common.events.EventService;
import common.events.types.CreateAIEntityEventType;
import networking.events.consumer.client.incoming.CreateEntityIncomingConsumerClient;
import networking.events.consumer.client.incoming.RemoveEntityIncomingConsumerClient;
import networking.events.consumer.client.incoming.ReplaceBlockIncomingConsumerClient;
import networking.events.consumer.client.incoming.UpdateEntityIncomingConsumerClient;
import networking.events.consumer.client.outgoing.CreateAIEntityConsumerClient;
import networking.events.consumer.client.outgoing.CreateEntityOutgoingConsumerClient;
import networking.events.consumer.client.outgoing.ReplaceBlockOutgoingConsumerClient;
import networking.events.consumer.client.outgoing.UpdateEntityOutgoingConsumerClient;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.incoming.RemoveEntityIncomingEventType;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;

public class ClientEventConsumer extends EventConsumer {
    @Inject
    EventService eventService;

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

    @Inject
    protected ClientEventConsumer() {
    }

    public void init() {
        super.init();
        this.eventService.addListener(CreateEntityIncomingEventType.type, createEntityIncomingConsumer);
        this.eventService.addListener(UpdateEntityIncomingEventType.type, updateEntityIncomingConsumer);
        this.eventService.addListener(RemoveEntityIncomingEventType.type, removeEntityIncomingConsumer);
        this.eventService.addListener(ReplaceBlockIncomingEventType.type, replaceBlockIncomingConsumer);
        this.eventService.addListener(CreateEntityOutgoingEventType.type, createEntityOutgoingConsumer);
        this.eventService.addListener(UpdateEntityOutgoingEventType.type, updateEntityOutgoingConsumer);
        this.eventService.addListener(ReplaceBlockOutgoingEventType.type, replaceBlockOutgoingConsumer);
        this.eventService.addPostUpdateListener(
                CreateAIEntityEventType.type, createAIEntityConsumerClient);
    }
}
