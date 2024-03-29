package core.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.chunk.Chunk;
import core.chunk.ChunkFactory;
import core.configuration.ClientConfig;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class testChunkClockMap {

  Injector injector;

  ChunkClockMap chunkClockMap;

  ChunkFactory chunkFactory;

  @Before
  public void setup() throws IOException {
    injector = Guice.createInjector(new ClientConfig());
    chunkClockMap = injector.getInstance(ChunkClockMap.class);
    chunkFactory = injector.getInstance(ChunkFactory.class);
  }

  @Test
  public void testExistence() {
    Chunk chunk =
        chunkFactory.create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(2, 1)));

    chunkClockMap.add(chunk);

    assert chunk
        == chunkClockMap.get(CommonFactory.createChunkRange(CommonFactory.createCoordinates(2, 1)));
  }

  @Test
  public void testGetChunksOnTick() {
    Chunk chunk =
        chunkFactory.create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)));
    chunkClockMap.add(chunk);
    assert chunkClockMap.getChunksOnTick(new Tick(1)).size() == 1;
    assert chunkClockMap.getChunksOnTick(new Tick(2)).size() == 0;
  }
}
