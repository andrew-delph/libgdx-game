package infra.entity.factories;

import infra.entity.EntityData;
import networking.NetworkObject;

import java.util.HashMap;
import java.util.List;

public class EntityDataFactory {
    static EntityDataFactory instance;

    public static EntityDataFactory getInstance() {
        if (instance == null) {
            instance = new EntityDataFactory();
        }
        return instance;
    }

    public EntityData createEntityData(NetworkObject.UpdateNetworkObject updateNetworkObject) {
        return this.createEntityData(updateNetworkObject.getId(), updateNetworkObject.getItemList());
    }

    public EntityData createEntityData(NetworkObject.CreateNetworkObject createNetworkObject) {
        return this.createEntityData(createNetworkObject.getId(), createNetworkObject.getItemList());
    }

    public EntityData createEntityData(NetworkObject.RemoveNetworkObject removeNetworkObject) {
        EntityData data = new EntityData();
        data.setId(removeNetworkObject.getId());
        return data;
    }

    public EntityData createEntityData(String id, List<NetworkObject.NetworkObjectItem> list) {
        EntityData updateData = new EntityData();
        updateData.setId(id);
        for (NetworkObject.NetworkObjectItem item : list) {
            System.out.println("item:"+item.getKey()+","+item.getValue());
            updateData.setItem(item.getKey(), item.getValue());
        }
        return updateData;
    }

    public EntityData createEntityData(HashMap<String, String> map) {
        return new EntityData(map);
    }
}
