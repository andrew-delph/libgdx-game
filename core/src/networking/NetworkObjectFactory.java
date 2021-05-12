package networking;

import com.google.inject.Inject;
import infra.entitydata.EntityData;

public class NetworkObjectFactory {
    @Inject
    NetworkObjectFactory() {
    }

    public NetworkObject.CreateNetworkObject createNetworkObject(EntityData entityData) {
        NetworkObject.NetworkObjectItem networkObjectItem_x =
                NetworkObject.NetworkObjectItem.newBuilder()
                        .setKey("x")
                        .setValue((entityData.getX()))
                        .build();
        NetworkObject.NetworkObjectItem networkObjectItem_y =
                NetworkObject.NetworkObjectItem.newBuilder()
                        .setKey("y")
                        .setValue((entityData.getY()))
                        .build();
        NetworkObject.NetworkObjectItem networkObjectItem_owner =
                NetworkObject.NetworkObjectItem.newBuilder().setKey("owner").setValue(entityData.getOwner()).build();
        NetworkObject.CreateNetworkObject createRequestObject =
                NetworkObject.CreateNetworkObject.newBuilder()
                        .setId(entityData.getID())
                        .addItem(networkObjectItem_x)
                        .addItem(networkObjectItem_y)
                        .addItem(networkObjectItem_owner)
                        .build();
        return createRequestObject;
    }

    public NetworkObject.UpdateNetworkObject updateNetworkObject(EntityData entityData) {
        NetworkObject.NetworkObjectItem networkObjectItem_x =
                NetworkObject.NetworkObjectItem.newBuilder()
                        .setKey("x")
                        .setValue((entityData.getX() + ""))
                        .build();
        NetworkObject.NetworkObjectItem networkObjectItem_y =
                NetworkObject.NetworkObjectItem.newBuilder()
                        .setKey("y")
                        .setValue((entityData.getY() + ""))
                        .build();
        NetworkObject.NetworkObjectItem networkObjectItem_owner =
                NetworkObject.NetworkObjectItem.newBuilder().setKey("owner").setValue(entityData.getOwner()).build();
        NetworkObject.UpdateNetworkObject updateRequestObject =
                NetworkObject.UpdateNetworkObject.newBuilder()
                        .setId(entityData.getID())
                        .addItem(networkObjectItem_x)
                        .addItem(networkObjectItem_y)
                        .addItem(networkObjectItem_owner)
                        .build();
        return updateRequestObject;
    }

    public NetworkObject.RemoveNetworkObject removeNetworkObject(EntityData entityData) {
        NetworkObject.RemoveNetworkObject removeNetworkObject =
                NetworkObject.RemoveNetworkObject.newBuilder().setId(entityData.getID()).build();
        return removeNetworkObject;
    }
}
