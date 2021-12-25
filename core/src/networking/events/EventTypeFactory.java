package networking.events;

import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.events.types.CreateAIEntityEventType;
import common.events.types.RemoveEntityEventType;
import common.events.types.ReplaceBlockEventType;
import entity.block.Block;
import networking.NetworkObjects;
import networking.events.types.incoming.*;
import networking.events.types.outgoing.*;
import networking.translation.NetworkDataDeserializer;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class EventTypeFactory {

    @Inject
    NetworkDataDeserializer entitySerializationConverter;

    @Inject
    EventTypeFactory() {
    }

    public CreateEntityOutgoingEventType createCreateEntityOutgoingEvent(
            NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
        return new CreateEntityOutgoingEventType(entityData, chunkRange);
    }

    public CreateEntityOutgoingEventType createCreateEntityOutgoingEvent(
            NetworkObjects.NetworkData entityData) {
        return new CreateEntityOutgoingEventType(entityData, null);
    }

    public CreateEntityIncomingEventType createCreateEntityIncomingEvent(
            NetworkObjects.NetworkEvent networkEvent) {
        return new CreateEntityIncomingEventType(networkEvent);
    }

    public UpdateEntityOutgoingEventType createUpdateEntityOutgoingEvent(
            NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
        return new UpdateEntityOutgoingEventType(entityData, chunkRange);
    }

    public UpdateEntityIncomingEventType createUpdateEntityIncomingEvent(
            NetworkObjects.NetworkEvent networkEvent) {
        return new UpdateEntityIncomingEventType(networkEvent);
    }

    public SubscriptionOutgoingEventType createSubscriptionOutgoingEvent(
            List<ChunkRange> chunkRangeList) {
        return new SubscriptionOutgoingEventType(chunkRangeList);
    }

    public SubscriptionIncomingEventType createSubscriptionIncomingEvent(
            NetworkObjects.NetworkEvent networkEvent) {
        return new SubscriptionIncomingEventType(networkEvent);
    }

    public RemoveEntityIncomingEventType createRemoveEntityIncomingEvent(
            NetworkObjects.NetworkEvent networkEvent) {
        return new RemoveEntityIncomingEventType(networkEvent);
    }

    public RemoveEntityOutgoingEventType createRemoveEntityOutgoingEvent(
            NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
        return new RemoveEntityOutgoingEventType(entityData, chunkRange);
    }

    public DisconnectionIncomingEventType createDisconnectionEvent(UUID uuid) {
        return new DisconnectionIncomingEventType(uuid);
    }

    public ReplaceBlockIncomingEventType createReplaceBlockIncomingEvent(
            NetworkObjects.NetworkEvent networkEvent) {
        return new ReplaceBlockIncomingEventType(networkEvent);
    }

    public ReplaceBlockOutgoingEventType createReplaceBlockOutgoingEvent(
            UUID target, Block replacementBlock, ChunkRange chunkRange) {
        return new ReplaceBlockOutgoingEventType(target, replacementBlock, chunkRange);
    }

    public ReplaceBlockEventType createReplaceBlockEvent(UUID target, Block replacementBlock) {
        return new ReplaceBlockEventType(target, replacementBlock);
    }

    public RemoveEntityEventType createRemoveEntityEvent(UUID entityUuid) {
        return new RemoveEntityEventType(entityUuid);
    }

    public CreateAIEntityEventType createAIEntityEventType(Coordinates coordinates) {
        return new CreateAIEntityEventType(coordinates);
    }

    public GetChunkOutgoingEventType createGetChunkOutgoingEventType(NetworkObjects.NetworkEvent networkEvent) {
        return new GetChunkOutgoingEventType(NetworkDataDeserializer.createChunkRange(networkEvent.getData()), UUID.fromString(networkEvent.getUser()));
    }

    public GetChunkOutgoingEventType createGetChunkOutgoingEventType(ChunkRange chunkRange, UUID userID) {
        return new GetChunkOutgoingEventType(chunkRange, userID);
    }

    public static HandshakeOutgoingEventType createHandshakeOutgoingEventType(ChunkRange chunkRange, List<UUID> listUUID) {
        return new HandshakeOutgoingEventType(chunkRange, listUUID);
    }

    public static HandshakeOutgoingEventType createHandshakeOutgoingEventType(ChunkRange chunkRange) {
        return new HandshakeOutgoingEventType(chunkRange, new LinkedList<>());
    }

    public static HandshakeIncomingEventType createHandshakeIncomingEventType(UUID requestUUID, ChunkRange chunkRange, List<UUID> listUUID) {
        return new HandshakeIncomingEventType(requestUUID, chunkRange, listUUID);
    }
}
