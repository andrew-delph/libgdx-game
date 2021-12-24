package networking.translation;

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
                .setKey(String.valueOf(TranslationEnum.UUID_LIST))
                .addAllChildren(dataList)
                .build();
    }

    public static NetworkObjects.NetworkData createUUID(UUID uuid) {
        return NetworkObjects.NetworkData.newBuilder()
                .setKey(String.valueOf(TranslationEnum.UUID)).setValue(uuid.toString()).build();
    }
}
