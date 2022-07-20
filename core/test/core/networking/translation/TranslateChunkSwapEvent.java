package core.networking.translation;

import core.chunk.ChunkRange;
import core.common.CommonFactory;
import core.common.exceptions.SerializationDataMissing;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.ChunkSwapIncomingEventType;
import core.networking.events.types.outgoing.ChunkSwapOutgoingEventType;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class TranslateChunkSwapEvent {

  @Test
  public void testTranslateChunkSwapEvent() throws SerializationDataMissing {
    ChunkRange from = new ChunkRange(CommonFactory.createCoordinates(0, 0));
    ChunkRange to = new ChunkRange(CommonFactory.createCoordinates(-1, 0));
    UUID target = UUID.randomUUID();

    ChunkSwapOutgoingEventType outgoing =
        EventTypeFactory.createChunkSwapOutgoingEventType(target, from, to);
    ChunkSwapIncomingEventType incoming =
        NetworkDataDeserializer.createChunkSwapIncomingEventType(
            NetworkDataSerializer.createChunkSwapOutgoingEventType(outgoing));

    assert incoming.getTarget().equals(outgoing.getTarget());
    Assert.assertEquals(incoming.getFrom(), outgoing.getFrom());
    Assert.assertEquals(incoming.getTo(), outgoing.getTo());
  }
}
