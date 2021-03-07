package networking;

import com.google.inject.Guice;
import com.google.inject.Injector;
import modules.App;
import networking.server.observers.ServerObserverFactory;
import networking.connetion.ConnectionStore;
import networking.connetion.CreateConnection;
import networking.connetion.UpdateConnection;
import networking.server.observers.CreateObserver;
import networking.server.observers.RemoveObserver;
import networking.server.observers.UpdateObserver;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestConnectionStore {

    @Test
    public void testGetAll() {

        Injector injector = Guice.createInjector(new App());
        ConnectionStore store = injector.getInstance(ConnectionStore.class);
        ServerObserverFactory observerFactory = injector.getInstance(ServerObserverFactory.class);
        CreateConnection c1 = new CreateConnection(null,null);
        store.add(c1);
        UpdateConnection c2 = new UpdateConnection(null,null);
        store.add(c2);
        UpdateConnection c3 = new UpdateConnection(null,null);
        System.out.println(c3);
        store.add(c3);

        List<UpdateConnection> returnedList = store.getAll(UpdateConnection.class);

        assertEquals(1, store.getAll(CreateConnection.class).toArray().length);
        assertEquals(c1,store.getAll(CreateConnection.class).toArray(new CreateConnection[0])[0]);

        assertEquals(2, store.getAll(UpdateConnection.class).toArray().length);
        System.out.println(store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[1]);
        assertEquals(c2,store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[0]);
        assertEquals(c3,store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[1]);

;
    }
}
