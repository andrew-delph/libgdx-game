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

        CreateObserver createObserver = observerFactory.createCreateObserver();
        UpdateObserver updateObserver = observerFactory.createUpdateObserver();
        UpdateObserver updateObserver2 = observerFactory.createUpdateObserver();
        RemoveObserver removeObserver = observerFactory.createRemoveObserver();

        store.add(new CreateConnection(createObserver));
        store.add(new UpdateConnection(updateObserver));
        store.add(new UpdateConnection(updateObserver2));

        List<UpdateConnection> returnedList = store.getAll(UpdateConnection.class);

        assertEquals(1, store.getAll(CreateConnection.class).toArray().length);
        assertEquals(store.getAll(CreateConnection.class).toArray(new CreateConnection[0])[0].responseObserver, createObserver);

        assertEquals(2, store.getAll(UpdateConnection.class).toArray().length);
        assertEquals(store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[0].responseObserver, updateObserver);
        assertEquals(store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[1].responseObserver, updateObserver2);

;
    }
}
