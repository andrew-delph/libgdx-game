package networking.singleclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import modules.App;
import networking.client.ClientNetworkHandle;
import networking.connetion.AbtractConnection;
import networking.connetion.ConnectionStore;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class DisconnectionTests {
    Injector serverInjector;
    ServerNetworkHandle server;

    @Before
    public void setup() throws IOException {
        serverInjector = Guice.createInjector(new App());
        server = serverInjector.getInstance(ServerNetworkHandle.class);
        server.start();
    }

    @After
    public void cleanup() {
        server.close();
    }

    @Test
    public void clientDisconnection() throws InterruptedException {

        Injector client_aInjector;
        ClientNetworkHandle client_a;
        client_aInjector = Guice.createInjector(new App());
        client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
        client_a.connect();
        ConnectionStore connectionStore = serverInjector.getInstance(ConnectionStore.class);
        TimeUnit.SECONDS.sleep(1);
        List serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(3,serverConnections.size());
        client_a.disconnect();
        TimeUnit.SECONDS.sleep(1);
        serverConnections = connectionStore.getAll(AbtractConnection.class);
        assertEquals(0,serverConnections.size());
    }
}
