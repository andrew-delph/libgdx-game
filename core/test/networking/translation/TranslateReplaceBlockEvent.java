package networking.translation;

import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.exceptions.SerializationDataMissing;
import configuration.ClientConfig;
import entity.block.Block;
import entity.block.BlockFactory;
import java.util.UUID;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import org.junit.Test;

public class TranslateReplaceBlockEvent {

  @Test
  public void translateReplaceBlockEvent() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());
    NetworkDataDeserializer networkDataDeserializer =
        injector.getInstance(NetworkDataDeserializer.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    UUID uuidToRemove = UUID.randomUUID();
    Coordinates coordinates = new Coordinates(0, 1);
    Block blockReplacement = blockFactory.createDirt(coordinates);
    ChunkRange chunkRange = new ChunkRange(coordinates);

    ReplaceBlockOutgoingEventType outgoing =
        EventTypeFactory.createReplaceBlockOutgoingEvent(
            uuidToRemove, blockReplacement, chunkRange);
    ReplaceBlockIncomingEventType incoming =
        networkDataDeserializer.createReplaceBlockIncomingEventType(
            NetworkDataSerializer.createReplaceBlockOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getTarget().equals(incoming.getTarget());
    assert outgoing.getReplacementEntity().equals(incoming.getReplacementBlock());
  }
}
