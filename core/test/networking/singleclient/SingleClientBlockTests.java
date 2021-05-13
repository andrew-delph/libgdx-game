package networking.singleclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.ClientTestApp;
import configure.ServerTestApp;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SingleClientBlockTests {
  Injector serverInjector;
  Injector client_aInjector;

  ServerNetworkHandle server;
  ClientNetworkHandle client_a;

  @Before
  public void setup() throws IOException {
    serverInjector = Guice.createInjector(new ServerTestApp());
    server = serverInjector.getInstance(ServerNetworkHandle.class);
    server.start();

    client_aInjector = Guice.createInjector(new ClientTestApp());
    client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
    client_a.connect();
  }

  @After
  public void cleanup() {
    client_a.disconnect();
    server.close();
  }

  @Test
  public void serverCreateBlock() throws InterruptedException {}
}
