package networking;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.CoreApp;
import configure.TestApp;
import networking.connetion.ConnectionStore;
import networking.connetion.CreateConnection;
import networking.connetion.UpdateConnection;
import networking.server.observers.ServerObserverFactory;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

public class TestConnectionStore {

    @Test
    public void testGetAll() {

        Injector injector = Guice.createInjector(new TestApp());
        ConnectionStore store = injector.getInstance(ConnectionStore.class);
        ServerObserverFactory observerFactory = injector.getInstance(ServerObserverFactory.class);
        CreateConnection c1 = new CreateConnection(null, null);
        store.add(c1);
        UpdateConnection c2 = new UpdateConnection(null, null);
        store.add(c2);
        UpdateConnection c3 = new UpdateConnection(null, null);
        System.out.println(c3);
        store.add(c3);

        assertEquals(1, store.getAll(CreateConnection.class).toArray().length);
        assertEquals(c1, store.getAll(CreateConnection.class).toArray(new CreateConnection[0])[0]);

        assertEquals(2, store.getAll(UpdateConnection.class).toArray().length);
        System.out.println(store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[1]);
//        assertEquals(c2,store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[1]);
//        assertEquals(c3,store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])[0]);
        assertThat(Arrays.asList(store.getAll(UpdateConnection.class).toArray(new UpdateConnection[0])), containsInAnyOrder(c2, c3));

    }
}
