package networking.translation;

import chunk.ChunkRange;
import networking.NetworkObjects;

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
                .setKey(String.valueOf(DataTranslationEnum.UUID_LIST))
                .addAllChildren(dataList)
                .build();
    }

    public static NetworkObjects.NetworkData createUUID(UUID uuid) {
        return NetworkObjects.NetworkData.newBuilder()
                .setKey(String.valueOf(DataTranslationEnum.UUID)).setValue(uuid.toString()).build();
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
}
