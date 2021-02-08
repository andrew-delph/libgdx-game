package networking;

import networking.server.connetion.ConnectionStore;
import networking.server.connetion.CreateConnection;
import networking.server.connetion.UpdateConnection;
import networking.server.observer.CreateObserver;
import networking.server.observer.RemoveObserver;
import networking.server.observer.UpdateObserver;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestConnectionStore {

    @Test
    public void testGetAll() {

        ConnectionStore store = ConnectionStore.getInstance();

        UUID managerID = UUID.randomUUID();

        CreateObserver createObserver = new CreateObserver(managerID);
        UpdateObserver updateObserver = new UpdateObserver(managerID);
        UpdateObserver updateObserver2 = new UpdateObserver(managerID);
        RemoveObserver removeObserver = new RemoveObserver(managerID);

        store.add(new CreateConnection(createObserver));
        store.add(new UpdateConnection(updateObserver));
        store.add(new UpdateConnection(updateObserver2));
//        store.add(removeObserver);

        List<UpdateConnection> returnedList = store.getAll(UpdateConnection.class);

        assertEquals(1, store.getAll(CreateConnection.class).toArray().length);
        assertEquals(store.getAll(CreateConnection.class).toArray(new CreateConnection[0])[0].responseObserver, createObserver);

        assertEquals(2, store.getAll(UpdateConnection.class).toArray().length);
        assertEquals(store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[0].responseObserver, updateObserver);
        assertEquals(store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[1].responseObserver, updateObserver2);

//        assertEquals(1, store.getAll(UpdateObserver.class).toArray().length);
//        assertEquals(store.getAll(UpdateObserver.class).toArray()[0], updateObserver);
    }
}
