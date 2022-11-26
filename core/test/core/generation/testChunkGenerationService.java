package core.generation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.ChunkRange;
import core.common.CommonFactory;
import core.common.GameStore;
import core.configuration.BaseServerConfig;
import core.entity.block.Block;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
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
    ChunkRange chunkRangeToTest =
        CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0));
    Assert.assertFalse(gameStore.doesChunkExist(chunkRangeToTest));

    chunkGenerationService.queueChunkRangeToGenerate(chunkRangeToTest);
    chunkGenerationService.queueChunkRangeToGenerate(chunkRangeToTest);
    chunkGenerationService.queueChunkRangeToGenerate(chunkRangeToTest);
    TimeUnit.SECONDS.sleep(1);

    Assert.assertTrue(gameStore.doesChunkExist(chunkRangeToTest));
    Assert.assertEquals(
        30,
        gameStore.getChunk(chunkRangeToTest).getEntityList().stream()
            .filter(entity -> entity instanceof Block)
            .collect(Collectors.toList())
            .size());
  }

  @Test
  public void testBlockedChunkGenerationService() throws Exception {
    ChunkRange chunkRangeToTest =
        CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0));
    Assert.assertFalse(gameStore.doesChunkExist(chunkRangeToTest));

    chunkGenerationService.blockedChunkRangeToGenerate(chunkRangeToTest);
    chunkGenerationService.blockedChunkRangeToGenerate(chunkRangeToTest);
    chunkGenerationService.blockedChunkRangeToGenerate(chunkRangeToTest);

    Assert.assertTrue(gameStore.doesChunkExist(chunkRangeToTest));
    Assert.assertEquals(
        30,
        gameStore.getChunk(chunkRangeToTest).getEntityList().stream()
            .filter(entity -> entity instanceof Block)
            .collect(Collectors.toList())
            .size());
  }
}
