package networking.events;

import app.user.UserID;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.events.types.CreateAIEntityEventType;
import common.events.types.RemoveEntityEventType;
import common.events.types.ReplaceEntityEventType;
import entity.Entity;
import entity.block.Block;
import networking.NetworkObjects;
import networking.RequestNetworkEventObserver;
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

    public static CreateEntityIncomingEventType createCreateEntityIncomingEvent(UserID userID, NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
        return new CreateEntityIncomingEventType(userID, networkData, chunkRange);
    }

    public static UpdateEntityOutgoingEventType createUpdateEntityOutgoingEvent(
            NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {
        return new UpdateEntityOutgoingEventType(entityData, chunkRange);
    }

    public static UpdateEntityIncomingEventType createUpdateEntityIncomingEvent(
            UserID user,
            NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
        return new UpdateEntityIncomingEventType(user, networkData, chunkRange);
    }

    public static RemoveEntityIncomingEventType createRemoveEntityIncomingEvent(UserID user, ChunkRange chunkRange, UUID target) {
        return new RemoveEntityIncomingEventType(user, chunkRange, target);
    }

    public static RemoveEntityOutgoingEventType createRemoveEntityOutgoingEvent(
            UUID target, ChunkRange chunkRange) {
        return new RemoveEntityOutgoingEventType(target, chunkRange);
    }

    public static ReplaceBlockIncomingEventType createReplaceBlockIncomingEvent(
            UserID user, UUID target, Block replacementBlock, ChunkRange chunkRange) {
        return new ReplaceBlockIncomingEventType(user, target, replacementBlock, chunkRange);
    }

    public static ReplaceBlockOutgoingEventType createReplaceBlockOutgoingEvent(
            UUID target, Entity replacementEntity, ChunkRange chunkRange) {
        return new ReplaceBlockOutgoingEventType(target, replacementEntity, chunkRange);
    }

    public static ReplaceEntityEventType createReplaceEntityEvent(UUID target, Entity replacementEntity, Boolean swapVelocity, ChunkRange chunkRange) {
        return new ReplaceEntityEventType(target, replacementEntity, swapVelocity, chunkRange);
    }

    public static HandshakeOutgoingEventType createHandshakeOutgoingEventType(ChunkRange chunkRange, List<UUID> listUUID) {
        return new HandshakeOutgoingEventType(chunkRange, listUUID);
    }

    public static HandshakeOutgoingEventType createHandshakeOutgoingEventType(ChunkRange chunkRange) {
        return new HandshakeOutgoingEventType(chunkRange, new LinkedList<>());
    }

    public static HandshakeIncomingEventType createHandshakeIncomingEventType(UserID requestUserID, ChunkRange chunkRange, List<UUID> listUUID) {
        return new HandshakeIncomingEventType(requestUserID, chunkRange, listUUID);
    }

    public static CreateAIEntityEventType createAIEntityEventType(Coordinates coordinates, UUID target) {
        return new CreateAIEntityEventType(coordinates, target);
    }

    public static CreateAIEntityEventType createAIEntityEventType(UUID target) {
        return new CreateAIEntityEventType(new Coordinates(0, 0), target);
    }

    public static ChunkSwapIncomingEventType createChunkSwapIncomingEventType(UUID target, ChunkRange from, ChunkRange to) {
        return new ChunkSwapIncomingEventType(target, from, to);
    }

    public static ChunkSwapOutgoingEventType createChunkSwapOutgoingEventType(UUID target, ChunkRange from, ChunkRange to) {
        return new ChunkSwapOutgoingEventType(target, from, to);
    }

    public SubscriptionOutgoingEventType createSubscriptionOutgoingEvent(
            List<ChunkRange> chunkRangeList) {
        return new SubscriptionOutgoingEventType(chunkRangeList);
    }

    public SubscriptionIncomingEventType createSubscriptionIncomingEvent(
            NetworkObjects.NetworkEvent networkEvent) {
        return new SubscriptionIncomingEventType(networkEvent);
    }

    public DisconnectionIncomingEventType createDisconnectionEvent(UserID userID) {
        return new DisconnectionIncomingEventType(userID);
    }

    public RemoveEntityEventType createRemoveEntityEvent(UUID entityUuid) {
        return new RemoveEntityEventType(entityUuid);
    }

    public GetChunkOutgoingEventType createGetChunkOutgoingEventType(NetworkObjects.NetworkEvent networkEvent) {
        return new GetChunkOutgoingEventType(NetworkDataDeserializer.createChunkRange(networkEvent.getData()), UserID.createUserID(networkEvent.getUser()));
    }

    public GetChunkOutgoingEventType createGetChunkOutgoingEventType(ChunkRange chunkRange, UserID userID) {
        return new GetChunkOutgoingEventType(chunkRange, userID);
    }

    public AuthenticationIncomingEventType createAuthenticationIncomingEventType(UserID userID, RequestNetworkEventObserver requestNetworkEventObserver) {
        return new AuthenticationIncomingEventType(userID, requestNetworkEventObserver);
    }
}
