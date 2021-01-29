package infra.factories;

import infra.entity.EntityData;
import infra.entity.factories.EntityDataFactory;
import networking.NetworkObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestEntityDataFactory {
    @Test
    public void createCreateNetworkObject() {

        String x = "0";
        String y = "0";
        String id = UUID.randomUUID().toString();

        NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue(x).build();
        NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((y)).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(id).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();

        EntityData data = EntityDataFactory.getInstance().createEntityData(createRequestObject);

        assertEquals(x, data.getX());
        assertEquals(y, data.getY());
        assertEquals(id, data.getID());
    }

    @Test
    public void createUpdateNetworkObject() {
        String x = "0";
        String y = "0";
        String id = UUID.randomUUID().toString();

        NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue(x).build();
        NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((y)).build();
        NetworkObject.UpdateNetworkObject updateRequestObject = NetworkObject.UpdateNetworkObject.newBuilder().setId(id).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();

        EntityData data = EntityDataFactory.getInstance().createEntityData(updateRequestObject);

        assertEquals(x, data.getX());
        assertEquals(y, data.getY());
        assertEquals(id, data.getID());
    }

    @Test
    public void createFromMap() {
        HashMap<String, String> map = new HashMap<>();

        String x = "0";
        String y = "0";
        String id = UUID.randomUUID().toString();

        map.put("x", x);
        map.put("y", y);
        map.put("id", id);

        EntityData data = EntityDataFactory.getInstance().createEntityData(map);

        assertEquals(x, data.getX());
        assertEquals(y, data.getY());
        assertEquals(id, data.getID());

    }

}
