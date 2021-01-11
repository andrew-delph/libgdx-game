package infra.entity.factories;

import infra.entity.EntityData;
import networking.NetworkObject;

public class EntityDataFactory {
    static EntityDataFactory instance;

    public static EntityDataFactory getInstance() {
        if (instance == null) {
            instance = new EntityDataFactory();
        }
        return instance;
    }

    EntityDataFactory() {

    }

    public EntityData createEntityData(NetworkObject.UpdateNetworkObject updateNetworkObject) {
        return null;
    }

    public EntityData createEntityData(NetworkObject.CreateNetworkObject createNetworkObject) {
        return null;
    }

    public EntityData createEntityData(NetworkObject.RemoveNetworkObject removeNetworkObject) {
        return null;
    }
}
