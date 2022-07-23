package core.networking.translation;

import core.chunk.ChunkRange;
import core.common.events.types.CreateAIEntityEventType;
import core.common.events.types.CreateTurretEventType;
import core.common.events.types.ItemActionEventType;
import core.entity.Entity;
import core.entity.attributes.Attribute;
import core.entity.attributes.inventory.Equipped;
import core.entity.attributes.inventory.item.EmptyInventoryItem;
import core.entity.attributes.inventory.item.OrbInventoryItem;
import core.entity.attributes.inventory.item.SwordInventoryItem;
import core.entity.attributes.msc.AnimationStateWrapper;
import core.entity.attributes.msc.Coordinates;
import core.entity.attributes.msc.Health;
import core.networking.events.interfaces.SerializeNetworkData;
import core.networking.events.types.outgoing.ChunkSwapOutgoingEventType;
import core.networking.events.types.outgoing.CreateEntityOutgoingEventType;
import core.networking.events.types.outgoing.PingRequestOutgoingEventType;
import core.networking.events.types.outgoing.PingResponseOutgoingEventType;
import core.networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import core.networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import networking.NetworkObjects;
import networking.NetworkObjects.NetworkData;

public class NetworkDataSerializer {

  public static NetworkObjects.NetworkData createEntity(Entity entity) {
    NetworkObjects.NetworkData uuid =
        NetworkObjects.NetworkData.newBuilder()
            .setKey(UUID.class.getName())
            .setValue(entity.getUuid().toString())
            .build();

    List<NetworkData> bagData =
        Arrays.stream(entity.getBag().getItemList())
            .map(SerializeNetworkData::toNetworkData)
            .collect(Collectors.toList());

    return NetworkObjects.NetworkData.newBuilder()
        .setKey("class")
        .setValue(entity.getClass().getName())
        .addChildren(entity.getCoordinatesWrapper().getCoordinates().toNetworkData())
        .addChildren(uuid)
        .addAllChildren(bagData)
        .addChildren(entity.health.toNetworkData())
        .addChildren(entity.getAnimationStateWrapper().toNetworkData())
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

  public static NetworkObjects.NetworkData createHealth(Health health) {
    return NetworkObjects.NetworkData.newBuilder()
        .setKey(DataTranslationEnum.HEALTH)
        .setValue(String.valueOf(health.getValue()))
        .build();
  }

  public static NetworkObjects.NetworkData createAnimationStateWrapper(
      AnimationStateWrapper animationStateWrapper) {
    return NetworkObjects.NetworkData.newBuilder()
        .setKey(DataTranslationEnum.ANIMATION_STATE)
        .setValue(String.valueOf(animationStateWrapper.getAnimationState()))
        .build();
  }

  public static NetworkObjects.NetworkData createEquipped(Equipped equipped) {
    return NetworkObjects.NetworkData.newBuilder()
        .setKey(DataTranslationEnum.EQUIPPED)
        .setValue(String.valueOf(equipped.getIndex()))
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
    dataListBuilder.addChildren(createUUID(updateEntityOutgoingEventType.getUuid()));

    for (Attribute attr : updateEntityOutgoingEventType.getAttributeList()) {
      dataListBuilder.addChildren(attr.toNetworkData());
    }
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

  public static NetworkObjects.NetworkData serializeEmptyInventoryItem(EmptyInventoryItem item) {

    NetworkObjects.NetworkData.Builder emptyItem =
        NetworkObjects.NetworkData.newBuilder().setKey(DataTranslationEnum.EMPTY_ITEM);

    emptyItem.addChildren(createIndex(item.getIndex()));

    return emptyItem.build();
  }

  public static NetworkObjects.NetworkData serializeOrbInventoryItem(OrbInventoryItem item) {

    NetworkObjects.NetworkData.Builder orbData =
        NetworkObjects.NetworkData.newBuilder().setKey(DataTranslationEnum.ORB_ITEM);

    orbData.addChildren(createIndex(item.getIndex()));

    return orbData.build();
  }

  public static NetworkObjects.NetworkData serializeSwordInventoryItem(SwordInventoryItem item) {

    NetworkObjects.NetworkData.Builder swordData =
        NetworkObjects.NetworkData.newBuilder().setKey(DataTranslationEnum.SWORD_ITEM);

    swordData.addChildren(createIndex(item.getIndex()));

    return swordData.build();
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

  public static NetworkObjects.NetworkData createType(String type) {
    return NetworkObjects.NetworkData.newBuilder()
        .setKey(DataTranslationEnum.TYPE)
        .setValue(type)
        .build();
  }

  public static NetworkObjects.NetworkData createIndex(Integer index) {
    return NetworkObjects.NetworkData.newBuilder()
        .setKey(DataTranslationEnum.INDEX)
        .setValue(String.valueOf(index))
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
    dataListBuilder.addChildren(createUUID(createTurretEventType.getEntityUUID()));
    return eventBuilder.setData(dataListBuilder).build();
  }

  public static NetworkObjects.NetworkEvent serializeItemActionEventType(
      ItemActionEventType itemActionEventType) {

    NetworkObjects.NetworkEvent.Builder eventBuilder =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.ITEM_ACTION);

    NetworkObjects.NetworkData.Builder dataListBuilder = NetworkObjects.NetworkData.newBuilder();

    dataListBuilder.addChildren(createType(itemActionEventType.getItemActionType().name()));
    dataListBuilder.addChildren(createUUID(itemActionEventType.getControleeUUID()));

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
