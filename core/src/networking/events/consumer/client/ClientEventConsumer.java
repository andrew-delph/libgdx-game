package networking.events.consumer.client;

import com.google.inject.Inject;
import common.events.EventConsumer;
import common.events.EventService;
import common.events.types.CreateAIEntityEventType;
import networking.events.consumer.client.incoming.*;
import networking.events.consumer.client.outgoing.*;
import networking.events.types.NetworkEventTypeEnum;
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
    HandshakeIncomingConsumerClient handshakeIncomingConsumerClient;
    @Inject
    HandshakeOutgoingConsumerClient handshakeOutgoingConsumerClient;
    @Inject
    RemoveEntityOutgoingConsumerClient removeEntityOutgoingConsumerClient;
    @Inject
    ChunkSwapIncomingConsumerClient chunkSwapIncomingConsumerClient;
    @Inject
    AuthenticationIncomingConsumerClient authenticationIncomingConsumerClient;

    @Inject
    protected ClientEventConsumer() {
    }

    public void init() {
        super.init();
        this.eventService.addListener(CreateEntityIncomingEventType.type, createEntityIncomingConsumer);
        this.eventService.addListener(UpdateEntityIncomingEventType.type, updateEntityIncomingConsumer);
        this.eventService.addPostUpdateListener(ReplaceBlockIncomingEventType.type, replaceBlockIncomingConsumer);
        this.eventService.addListener(CreateEntityOutgoingEventType.type, createEntityOutgoingConsumer);
        this.eventService.addListener(UpdateEntityOutgoingEventType.type, updateEntityOutgoingConsumer);
        this.eventService.addListener(ReplaceBlockOutgoingEventType.type, replaceBlockOutgoingConsumer);
        this.eventService.addPostUpdateListener(CreateAIEntityEventType.type, createAIEntityConsumerClient);
        this.eventService.addListener(NetworkEventTypeEnum.HANDSHAKE_INCOMING, handshakeIncomingConsumerClient);
        this.eventService.addListener(NetworkEventTypeEnum.HANDSHAKE_OUTGOING, handshakeOutgoingConsumerClient);
        this.eventService.addListener(NetworkEventTypeEnum.REMOVE_ENTITY_OUTGOING, removeEntityOutgoingConsumerClient);
        this.eventService.addPostUpdateListener(RemoveEntityIncomingEventType.type, removeEntityIncomingConsumer);
        this.eventService.addPostUpdateListener(NetworkEventTypeEnum.CHUNK_SWAP_INCOMING, chunkSwapIncomingConsumerClient);
        this.eventService.addListener(NetworkEventTypeEnum.AUTH_INCOMING, authenticationIncomingConsumerClient);
    }
}
