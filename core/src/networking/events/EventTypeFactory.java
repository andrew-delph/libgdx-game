package networking.events;

import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.events.types.CreateAIEntityEventType;
import common.events.types.RemoveEntityEventType;
import common.events.types.ReplaceEntityEventType;
import entity.Entity;
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

    public static CreateEntityOutgoingEventType createCreateEntityOutgoingEvent(
            NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
        return new CreateEntityOutgoingEventType(entityData, chunkRange);
    }

    public static CreateEntityIncomingEventType createCreateEntityIncomingEvent(UUID user, NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
        return new CreateEntityIncomingEventType(user, networkData, chunkRange);
    }

    public static UpdateEntityOutgoingEventType createUpdateEntityOutgoingEvent(
            NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
        return new UpdateEntityOutgoingEventType(entityData, chunkRange);
    }

    public static UpdateEntityIncomingEventType createUpdateEntityIncomingEvent(
            UUID user,
            NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
        return new UpdateEntityIncomingEventType(user, networkData, chunkRange);
    }

    public SubscriptionOutgoingEventType createSubscriptionOutgoingEvent(
            List<ChunkRange> chunkRangeList) {
        return new SubscriptionOutgoingEventType(chunkRangeList);
    }

    public SubscriptionIncomingEventType createSubscriptionIncomingEvent(
            NetworkObjects.NetworkEvent networkEvent) {
        return new SubscriptionIncomingEventType(networkEvent);
    }

    public static RemoveEntityIncomingEventType createRemoveEntityIncomingEvent(UUID user, ChunkRange chunkRange, UUID target) {
        return new RemoveEntityIncomingEventType(user, chunkRange, target);
    }

    public static RemoveEntityOutgoingEventType createRemoveEntityOutgoingEvent(
            UUID target, ChunkRange chunkRange) {
        return new RemoveEntityOutgoingEventType(target, chunkRange);
    }

    public DisconnectionIncomingEventType createDisconnectionEvent(UUID uuid) {
        return new DisconnectionIncomingEventType(uuid);
    }

    public static ReplaceBlockIncomingEventType createReplaceBlockIncomingEvent(
            UUID user, UUID target, Block replacementBlock, ChunkRange chunkRange) {
        return new ReplaceBlockIncomingEventType(user, target, replacementBlock, chunkRange);
    }

    public static ReplaceBlockOutgoingEventType createReplaceBlockOutgoingEvent(
            UUID target, Entity replacementEntity, ChunkRange chunkRange) {
        return new ReplaceBlockOutgoingEventType(target, replacementEntity, chunkRange);
    }

    public static ReplaceEntityEventType createReplaceEntityEvent(UUID target, Entity replacementEntity, ChunkRange chunkRange) {
        return new ReplaceEntityEventType(target, replacementEntity, chunkRange);
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
