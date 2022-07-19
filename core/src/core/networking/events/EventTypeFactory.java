package core.networking.events;

import com.google.inject.Inject;
import core.app.user.UserID;
import core.chunk.ChunkRange;
import core.common.events.types.CreateAIEntityEventType;
import core.common.events.types.CreateTurretEventType;
import core.common.events.types.RemoveEntityEventType;
import core.common.events.types.ReplaceEntityEventType;
import core.entity.Entity;
import core.entity.attributes.Attribute;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.Block;
import core.networking.RequestNetworkEventObserver;
import core.networking.events.types.incoming.AuthenticationIncomingEventType;
import core.networking.events.types.incoming.ChunkSwapIncomingEventType;
import core.networking.events.types.incoming.CreateEntityIncomingEventType;
import core.networking.events.types.incoming.DisconnectionIncomingEventType;
import core.networking.events.types.incoming.HandshakeIncomingEventType;
import core.networking.events.types.incoming.PingRequestIncomingEventType;
import core.networking.events.types.incoming.PingResponseIncomingEventType;
import core.networking.events.types.incoming.RemoveEntityIncomingEventType;
import core.networking.events.types.incoming.ReplaceBlockIncomingEventType;
import core.networking.events.types.incoming.SubscriptionIncomingEventType;
import core.networking.events.types.incoming.UpdateEntityIncomingEventType;
import core.networking.events.types.outgoing.ChunkSwapOutgoingEventType;
import core.networking.events.types.outgoing.CreateEntityOutgoingEventType;
import core.networking.events.types.outgoing.GetChunkOutgoingEventType;
import core.networking.events.types.outgoing.HandshakeOutgoingEventType;
import core.networking.events.types.outgoing.PingRequestOutgoingEventType;
import core.networking.events.types.outgoing.PingResponseOutgoingEventType;
import core.networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import core.networking.events.types.outgoing.SubscriptionOutgoingEventType;
import core.networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import core.networking.translation.NetworkDataDeserializer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import networking.NetworkObjects;

public class EventTypeFactory {

  @Inject
  EventTypeFactory() {}

  public static CreateTurretEventType createTurretEventType(
      UUID entityUUID, Coordinates coordinates) {
    return new CreateTurretEventType(entityUUID, coordinates);
  }

  public static CreateEntityOutgoingEventType createCreateEntityOutgoingEvent(
      NetworkObjects.NetworkData entityData, ChunkRange chunkRange) {

    return new CreateEntityOutgoingEventType(entityData, chunkRange);
  }

  public static CreateEntityIncomingEventType createCreateEntityIncomingEvent(
      UserID userID, NetworkObjects.NetworkData networkData, ChunkRange chunkRange) {
    return new CreateEntityIncomingEventType(userID, networkData, chunkRange);
  }

  public static UpdateEntityOutgoingEventType createUpdateEntityOutgoingEvent(
      List<Attribute> attributeList, ChunkRange chunkRange, UUID uuid) {
    return new UpdateEntityOutgoingEventType(attributeList, chunkRange, uuid);
  }

  public static UpdateEntityOutgoingEventType createUpdateEntityOutgoingEvent(
      Attribute attribute, ChunkRange chunkRange, UUID uuid) {
    return new UpdateEntityOutgoingEventType(Arrays.asList(attribute), chunkRange, uuid);
  }

  public static UpdateEntityIncomingEventType createUpdateEntityIncomingEvent(
      UserID userID, List<Attribute> attributeList, ChunkRange chunkRange, UUID uuid) {
    return new UpdateEntityIncomingEventType(userID, attributeList, chunkRange, uuid);
  }

  public static UpdateEntityIncomingEventType createUpdateEntityIncomingEvent(
      UserID userID, Attribute attribute, ChunkRange chunkRange, UUID uuid) {
    return new UpdateEntityIncomingEventType(userID, Arrays.asList(attribute), chunkRange, uuid);
  }

  public static RemoveEntityIncomingEventType createRemoveEntityIncomingEvent(
      UserID user, ChunkRange chunkRange, UUID target) {
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

  public static ReplaceEntityEventType createReplaceEntityEvent(
      UUID target, Entity replacementEntity, Boolean swapVelocity, ChunkRange chunkRange) {
    return new ReplaceEntityEventType(target, replacementEntity, swapVelocity, chunkRange);
  }

  public static HandshakeOutgoingEventType createHandshakeOutgoingEventType(
      ChunkRange chunkRange, List<UUID> listUUID) {
    return new HandshakeOutgoingEventType(chunkRange, listUUID);
  }

  public static HandshakeOutgoingEventType createHandshakeOutgoingEventType(ChunkRange chunkRange) {
    return new HandshakeOutgoingEventType(chunkRange, new LinkedList<>());
  }

  public static HandshakeIncomingEventType createHandshakeIncomingEventType(
      UserID requestUserID, ChunkRange chunkRange, List<UUID> listUUID) {
    return new HandshakeIncomingEventType(requestUserID, chunkRange, listUUID);
  }

  public static CreateAIEntityEventType createAIEntityEventType(
      Coordinates coordinates, UUID target) {
    return new CreateAIEntityEventType(coordinates, target);
  }

  public static CreateAIEntityEventType createAIEntityEventType(UUID target) {
    return new CreateAIEntityEventType(new Coordinates(0, 0), target);
  }

  public static ChunkSwapIncomingEventType createChunkSwapIncomingEventType(
      UUID target, ChunkRange from, ChunkRange to) {
    return new ChunkSwapIncomingEventType(target, from, to);
  }

  public static ChunkSwapOutgoingEventType createChunkSwapOutgoingEventType(
      UUID target, ChunkRange from, ChunkRange to) {
    return new ChunkSwapOutgoingEventType(target, from, to);
  }

  public static AuthenticationIncomingEventType createAuthenticationIncomingEventType(
      UserID userID, RequestNetworkEventObserver requestNetworkEventObserver) {
    return new AuthenticationIncomingEventType(userID, requestNetworkEventObserver);
  }

  public static PingRequestOutgoingEventType createPingRequestOutgoingEventType(
      UserID userID, UUID pingID) {
    return new PingRequestOutgoingEventType(userID, pingID);
  }

  public static PingResponseOutgoingEventType createPingResponseOutgoingEventType(UUID pingID) {
    return new PingResponseOutgoingEventType(pingID);
  }

  public static PingRequestIncomingEventType createPingRequestIncomingEventType(
      UserID userID, UUID pingID) {
    return new PingRequestIncomingEventType(userID, pingID);
  }

  public static PingResponseIncomingEventType createPingResponseIncomingEventType(
      UserID userID, UUID pingID, Long receivedTime) {
    return new PingResponseIncomingEventType(userID, pingID, receivedTime);
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

  public GetChunkOutgoingEventType createGetChunkOutgoingEventType(
      NetworkObjects.NetworkEvent networkEvent) {
    return new GetChunkOutgoingEventType(
        NetworkDataDeserializer.createChunkRange(networkEvent.getData()),
        UserID.createUserID(networkEvent.getUser()));
  }

  public GetChunkOutgoingEventType createGetChunkOutgoingEventType(
      ChunkRange chunkRange, UserID userID) {
    return new GetChunkOutgoingEventType(chunkRange, userID);
  }
}
