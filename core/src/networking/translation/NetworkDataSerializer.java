package networking.translation;

import chunk.ChunkRange;
import networking.NetworkObjects;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class NetworkDataSerializer {

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
                .setKey(DataTranslationEnum.UUID).setValue(uuid.toString()).build();
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

    public static NetworkObjects.NetworkEvent createReplaceBlockOutgoingEventType(ReplaceBlockOutgoingEventType replaceBlockOutgoingEventType) {
        NetworkObjects.NetworkEvent.Builder eventBuilder =
                NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.REPLACE_BLOCK);

        NetworkObjects.NetworkData.Builder dataListBuilder =
                NetworkObjects.NetworkData.newBuilder();

        dataListBuilder.addChildren(createChunkRange(replaceBlockOutgoingEventType.getChunkRange()));
        dataListBuilder.addChildren(createUUID(replaceBlockOutgoingEventType.getTarget()));
        dataListBuilder.addChildren(replaceBlockOutgoingEventType.getReplacementBlock().toNetworkData());

        return eventBuilder.setData(dataListBuilder).build();
    }

    public static NetworkObjects.NetworkEvent createUpdateEntityOutgoingEventType(UpdateEntityOutgoingEventType updateEntityOutgoingEventType) {
        NetworkObjects.NetworkEvent.Builder eventBuilder =
                NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.UPDATE_ENTITY);

        NetworkObjects.NetworkData.Builder dataListBuilder =
                NetworkObjects.NetworkData.newBuilder();

        dataListBuilder.addChildren(createChunkRange(updateEntityOutgoingEventType.getChunkRange()));
        dataListBuilder.addChildren(updateEntityOutgoingEventType.getEntityData());

        return eventBuilder.setData(dataListBuilder).build();
    }

    public static NetworkObjects.NetworkEvent createCreateEntityOutgoingEventType(CreateEntityOutgoingEventType createEntityOutgoingEventType) {
        NetworkObjects.NetworkEvent.Builder eventBuilder =
                NetworkObjects.NetworkEvent.newBuilder().setEvent(DataTranslationEnum.CREATE_ENTITY);

        NetworkObjects.NetworkData.Builder dataListBuilder =
                NetworkObjects.NetworkData.newBuilder();

        dataListBuilder.addChildren(createChunkRange(createEntityOutgoingEventType.getChunkRange()));
        dataListBuilder.addChildren(createEntityOutgoingEventType.getEntityData());

        return eventBuilder.setData(dataListBuilder).build();
    }
}
