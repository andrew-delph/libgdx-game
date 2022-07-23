package core.networking.translation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.chunk.Chunk;
import core.chunk.ChunkFactory;
import core.common.ChunkRange;
import core.common.CommonFactory;
import core.common.exceptions.SerializationDataMissing;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import org.junit.Test;

public class testChunkSerialization {

  Injector injector = Guice.createInjector(new ClientConfig());
  NetworkDataDeserializer networkDataSerialization;

  @Test
  public void testChunkSerialization() throws SerializationDataMissing {
    networkDataSerialization = injector.getInstance(NetworkDataDeserializer.class);
    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);
    Chunk chunk1 =
        chunkFactory.create(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0)));
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    for (int i = 0; i < 10; i++) {
      Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));
      chunk1.addEntity(entity);
    }

    Chunk chunk2 = networkDataSerialization.createChunk(chunk1.toNetworkData());

    assert chunk1.equals(chunk2);
  }

  @Test
  public void testChunkSerializationChunkRange() {

    networkDataSerialization = injector.getInstance(NetworkDataDeserializer.class);

    ChunkRange chunkRange = CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 0));
    assert chunkRange.equals(NetworkDataDeserializer.createChunkRange(chunkRange.toNetworkData()));

    ChunkRange chunkRange2 = CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, 0));
    assert chunkRange2.equals(
        NetworkDataDeserializer.createChunkRange(chunkRange2.toNetworkData()));
  }
}
