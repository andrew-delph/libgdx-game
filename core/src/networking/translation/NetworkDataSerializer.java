package networking.translation;

import chunk.ChunkRange;
import entity.attributes.Coordinates;
import common.events.types.CreateAIEntityEventType;
import common.events.types.CreateTurretEventType;
import entity.Entity;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import networking.NetworkObjects;
import networking.events.types.outgoing.ChunkSwapOutgoingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.events.types.outgoing.PingRequestOutgoingEventType;
import networking.events.types.outgoing.PingResponseOutgoingEventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;

public class NetworkDataSerializer {

  public static NetworkObjects.NetworkData createEntity(Entity entity) {
    NetworkObjects.NetworkData uuid =
        NetworkObjects.NetworkData.newBuilder()
            .setKey(UUID.class.getName())
            .setValue(entity.getUuid().toString())
            .build();
    return NetworkObjects.NetworkData.newBuilder()
        .setKey("class")
        .setValue(entity.getClass().getName())
        .addChildren(entity.coordinates.toNetworkData())
        .addChildren(uuid)
        .build();
  }

  public static NetworkObjects.NetworkData createUUIDList(List<UUID> uuidList) {
    List<NetworkObjects.NetworkData> dataList = new LinkedList<>();
    for (UUID uuid : uuidList) {
      dataList.add(NetworkDataSerializer.createUUID(uuid));
    }
    return NetworkObjects.NetworkData.newBuilder()
        .setKey(DataTranslationEnum.UUID_LIST)
        .addAllChildren(dataList)
        .build();
  }

  public static NetworkObjects.NetworkData createUUID(UUID uuid) {
    return NetworkObjects.NetworkData.newBuilder()
        .setKey(DataTranslationEnum.UUID)
        .setValue(uuid.toString())
        .build();
  }

  public static NetworkObjects.NetworkData createChunkRange(ChunkRange chunkRange) {
    NetworkObjects.NetworkData.Builder builder =
        NetworkObjects.NetworkData.newBuilder().setKey(DataTranslationEnum.CHUNK_RANGE);
    NetworkObjects.NetworkData x =
        NetworkObjects.NetworkData.newBuilder()
            .setKey("x")
            .setValue(String.valueOf(chunkRange.bottom_x))
            .build();
    NetworkObjects.NetworkData y =
        NetworkObjects.NetworkData.newBuilder()
            .setKey("y")
            .setValue(String.valueOf(chunkRange.bottom_y))
            .build();
    return builder.addChildren(x).addChildren(y).build();
  }

  public static NetworkObjects.NetworkEvent createReplaceBlockOutgoingEventType(
      ReplaceBlockOutgoingEventType replaceBlockOutgoingEventType) {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.REPLACE_BLOCK);

    NetworkObjects.NetworkData.Builder dataListBuilder = NetworkObjects.NetworkData.newBuilder();

    dataListBuilder.addChildren(createChunkRange(replaceBlockOutgoingEventType.getChunkRange()));
    dataListBuilder.addChildren(createUUID(replaceBlockOutgoingEventType.getTarget()));
    dataListBuilder.addChildren(
        replaceBlockOutgoingEventType.getReplacementEntity().toNetworkData());

    return eventBuilder.setData(dataListBuilder).build();
  }

  public static NetworkObjects.NetworkEvent createUpdateEntityOutgoingEventType(
      UpdateEntityOutgoingEventType updateEntityOutgoingEventType) {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.UPDATE_ENTITY);

    NetworkObjects.NetworkData.Builder dataListBuilder = NetworkObjects.NetworkData.newBuilder();

    dataListBuilder.addChildren(createChunkRange(updateEntityOutgoingEventType.getChunkRange()));
    dataListBuilder.addChildren(updateEntityOutgoingEventType.getEntityData());

    return eventBuilder.setData(dataListBuilder).build();
  }

  public static NetworkObjects.NetworkEvent createCreateEntityOutgoingEventType(
      CreateEntityOutgoingEventType createEntityOutgoingEventType) {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.CREATE_ENTITY);

    NetworkObjects.NetworkData.Builder dataListBuilder = NetworkObjects.NetworkData.newBuilder();

    dataListBuilder.addChildren(createChunkRange(createEntityOutgoingEventType.getChunkRange()));
    dataListBuilder.addChildren(createEntityOutgoingEventType.getEntityData());

    return eventBuilder.setData(dataListBuilder).build();
  }

  public static NetworkObjects.NetworkEvent createRemoveEntityOutgoingEventType(
      RemoveEntityOutgoingEventType removeEntityOutgoingEventType) {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.REMOVE_ENTITY);
    NetworkObjects.NetworkData.Builder dataListBuilder = NetworkObjects.NetworkData.newBuilder();
    dataListBuilder.addChildren(createChunkRange(removeEntityOutgoingEventType.getChunkRange()));
    dataListBuilder.addChildren(createUUID(removeEntityOutgoingEventType.getTarget()));
    return eventBuilder.setData(dataListBuilder).build();
  }

  public static NetworkObjects.NetworkData createCoordinates(Coordinates coordinates) {
    NetworkObjects.NetworkData x =
        NetworkObjects.NetworkData.newBuilder()
            .setKey("x")
            .setValue(String.valueOf(coordinates.getXReal()))
            .build();
    NetworkObjects.NetworkData y =
        NetworkObjects.NetworkData.newBuilder()
            .setKey("y")
            .setValue(String.valueOf(coordinates.getYReal()))
            .build();
    return NetworkObjects.NetworkData.newBuilder()
        .setKey(DataTranslationEnum.COORDINATES)
        .addChildren(x)
        .addChildren(y)
        .build();
  }

  public static NetworkObjects.NetworkEvent createCreateAIEntityEventType(
      CreateAIEntityEventType createAIEntityEventType) {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.CREATE_AI);
    NetworkObjects.NetworkData.Builder dataListBuilder = NetworkObjects.NetworkData.newBuilder();
    dataListBuilder.addChildren(createCoordinates(createAIEntityEventType.getCoordinates()));
    dataListBuilder.addChildren(createUUID(createAIEntityEventType.getTarget()));
    return eventBuilder.setData(dataListBuilder).build();
  }

  public static NetworkObjects.NetworkEvent serializeCreateTurretEventType(
      CreateTurretEventType createTurretEventType) {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.CREATE_TURRET);
    NetworkObjects.NetworkData.Builder dataListBuilder = NetworkObjects.NetworkData.newBuilder();
    dataListBuilder.addChildren(createCoordinates(createTurretEventType.getCoordinates()));
    return eventBuilder.setData(dataListBuilder).build();
  }

  public static NetworkObjects.NetworkEvent createChunkSwapOutgoingEventType(
      ChunkSwapOutgoingEventType chunkSwapOutgoingEventType) {
    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.CHUNK_SWAP);

    NetworkObjects.NetworkData.Builder dataListBuilder = NetworkObjects.NetworkData.newBuilder();

    dataListBuilder.addChildren(createUUID(chunkSwapOutgoingEventType.getTarget()));
    dataListBuilder.addChildren(
        createChunkRange(chunkSwapOutgoingEventType.getFrom()).toBuilder().setKey("from"));
    dataListBuilder.addChildren(
        createChunkRange(chunkSwapOutgoingEventType.getTo()).toBuilder().setKey("to"));

    return eventBuilder.setData(dataListBuilder).build();
  }

  public static NetworkObjects.NetworkEvent createPingRequestOutgoingEventType(
      PingRequestOutgoingEventType pingRequestOutgoingEventType) {

    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.REQUEST_PING);

    NetworkObjects.NetworkData.Builder dataBuilder = NetworkObjects.NetworkData.newBuilder();

    NetworkObjects.NetworkData pingID = createUUID(pingRequestOutgoingEventType.getPingID());

    dataBuilder.addChildren(pingID);

    return eventBuilder.setData(dataBuilder).build();
  }

  public static NetworkObjects.NetworkEvent createPingResponseOutgoingEventType(
      PingResponseOutgoingEventType pingResponseOutgoingEventType) {

    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.RESPONSE_PING);

    NetworkObjects.NetworkData.Builder dataBuilder = NetworkObjects.NetworkData.newBuilder();

    NetworkObjects.NetworkData pingID = createUUID(pingResponseOutgoingEventType.getPingID());

    dataBuilder.addChildren(pingID);

    return eventBuilder.setData(dataBuilder).build();
  }
}
