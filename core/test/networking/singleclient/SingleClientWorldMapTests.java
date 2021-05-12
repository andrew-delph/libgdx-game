package networking.singleclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.ClientTestApp;
import configure.ServerTestApp;
import infra.map.WorldMap;
import infra.map.block.Block;
import networking.client.ClientNetworkHandle;
import networking.server.ServerNetworkHandle;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SingleClientWorldMapTests {
  @Test
  public void test() throws InterruptedException, IOException {
    Injector serverInjector;
    Injector client_aInjector;

    ServerNetworkHandle server;
    ClientNetworkHandle client_a;

    serverInjector = Guice.createInjector(new ServerTestApp());
    server = serverInjector.getInstance(ServerNetworkHandle.class);
    server.start();

    client_aInjector = Guice.createInjector(new ClientTestApp());
    client_a = client_aInjector.getInstance(ClientNetworkHandle.class);
    client_a.connect();

    WorldMap serverWorldMap = serverInjector.getInstance(WorldMap.class);
    WorldMap clientWorldMap = serverInjector.getInstance(WorldMap.class);

    serverWorldMap.generateArea(0, 0, 10, 10);
    List<Block> serverBlocks = serverWorldMap.getBlocksInRange(0, 0, 10, 10);

    List<Block> clientBlocks = clientWorldMap.getBlocksInRange(0, 0, 10, 10);

    assertEquals(serverBlocks, clientBlocks);
  }
}
