package generation;

import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.GameSettings;
import common.GameStore;
import configuration.BaseServerConfig;
import entity.attributes.Coordinates;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testChunkGenerationService {

  ChunkGenerationService chunkGenerationService;
  Injector injector;
  GameStore gameStore;

  @Before
  public void setup() {
    injector = Guice.createInjector(new BaseServerConfig());
    chunkGenerationService = injector.getInstance(ChunkGenerationService.class);
    gameStore = injector.getInstance(GameStore.class);
  }

  @Test
  public void testQueueChunkGenerationService() throws InterruptedException {
    ChunkRange chunkRangeToTest = new ChunkRange(new Coordinates(0, 0));
    Assert.assertFalse(gameStore.doesChunkExist(chunkRangeToTest));

    chunkGenerationService.queueChunkRangeToGenerate(chunkRangeToTest);
    chunkGenerationService.queueChunkRangeToGenerate(chunkRangeToTest);
    chunkGenerationService.queueChunkRangeToGenerate(chunkRangeToTest);
    TimeUnit.SECONDS.sleep(1);

    Assert.assertTrue(gameStore.doesChunkExist(chunkRangeToTest));
    Assert.assertEquals(
        gameStore.getChunk(chunkRangeToTest).getEntityList().size(),
        GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE);
  }

  @Test
  public void testBlockedChunkGenerationService() throws Exception {
    ChunkRange chunkRangeToTest = new ChunkRange(new Coordinates(0, 0));
    Assert.assertFalse(gameStore.doesChunkExist(chunkRangeToTest));

    chunkGenerationService.blockedChunkRangeToGenerate(chunkRangeToTest);
    chunkGenerationService.blockedChunkRangeToGenerate(chunkRangeToTest);
    chunkGenerationService.blockedChunkRangeToGenerate(chunkRangeToTest);

    Assert.assertTrue(gameStore.doesChunkExist(chunkRangeToTest));
    Assert.assertEquals(
        GameSettings.CHUNK_SIZE * GameSettings.CHUNK_SIZE,
        gameStore.getChunk(chunkRangeToTest).getEntityList().size());
  }
}
