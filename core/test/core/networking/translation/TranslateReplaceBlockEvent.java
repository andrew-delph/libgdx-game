package core.networking.translation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.ChunkRange;
import core.common.CommonFactory;
import core.common.Coordinates;
import core.common.exceptions.SerializationDataMissing;
import core.configuration.ClientConfig;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.ReplaceBlockIncomingEventType;
import core.networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import java.util.UUID;
import org.junit.Test;

public class TranslateReplaceBlockEvent {

  @Test
  public void translateReplaceBlockEvent() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());
    NetworkDataDeserializer networkDataDeserializer =
        injector.getInstance(NetworkDataDeserializer.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);

    UUID uuidToRemove = UUID.randomUUID();
    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    Block blockReplacement = blockFactory.createDirt(coordinates);
    ChunkRange chunkRange = CommonFactory.createChunkRange(coordinates);

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
