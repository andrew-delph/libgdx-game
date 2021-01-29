package networking;

import infra.entity.Entity;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class TestEntityNetworking {
    @Test
    public void singleClientServer() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(99).addService(new ServerNetworkHandle()).build().start();

        ClientNetworkHandle client_a = ClientNetworkHandle.getInstance();
        client_a.connect();

        UUID testID = UUID.randomUUID();
        int x = 6;
        int y = 7;

        Entity testEntity = EntityFactory.getInstance().create(testID, x, y);
        NetworkObject.NetworkObjectItem createNetworkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem createNetworkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID()).addItem(createNetworkObjectItem_x).addItem(createNetworkObjectItem_y).build();
        client_a.createRequest.onNext(createRequestObject);

        TimeUnit.SECONDS.sleep(1);

        Entity recievedEntity = EntityManager.getInstance().get(testID);

        assertNotNull(recievedEntity);
        assertEquals(recievedEntity.getX(),x);
        assertEquals(recievedEntity.getY(),y);

        TimeUnit.SECONDS.sleep(1);

        testEntity.moveX(2);
        testEntity.moveY(3);

        NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((testEntity.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((testEntity.getEntityData().getY() + "")).build();
        NetworkObject.UpdateNetworkObject updateRequestObject = NetworkObject.UpdateNetworkObject.newBuilder().setId(testEntity.getEntityData().getID().toString()).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();
        client_a.updateRequest.onNext(updateRequestObject);

        TimeUnit.SECONDS.sleep(1);

        assertEquals(recievedEntity.getID(),testID);
        assertEquals(recievedEntity.getX(),x+2);
        assertEquals(recievedEntity.getY(),y+3);
    }

}
