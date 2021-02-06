package networking;

import networking.server.ConnectionStore;
import networking.server.CreateObserver;
import networking.server.RemoveObserver;
import networking.server.UpdateObserver;
import org.junit.Test;

import java.util.LinkedList;
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

        store.add(createObserver);
        store.add(updateObserver);
        store.add(updateObserver2);
        store.add(removeObserver);

        List<UpdateObserver> returnedList = store.getAll(UpdateObserver.class);

        assertEquals(1, store.getAll(CreateObserver.class).toArray().length);
        assertEquals(store.getAll(CreateObserver.class).toArray()[0], createObserver);

        assertEquals(2, store.getAll(UpdateObserver.class).toArray().length);
        assertEquals(store.getAll(UpdateObserver.class).toArray()[0], updateObserver);
        assertEquals(store.getAll(UpdateObserver.class).toArray()[1], updateObserver2);

//        assertEquals(1, store.getAll(UpdateObserver.class).toArray().length);
//        assertEquals(store.getAll(UpdateObserver.class).toArray()[0], updateObserver);
    }
}
