package networking.translation;

import chunk.ChunkRange;
import common.Coordinates;
import common.exceptions.SerializationDataMissing;
import java.util.UUID;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.RemoveEntityIncomingEventType;
import networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import org.junit.Test;

public class TranslateRemoveEntityEvent {

  @Test
  public void testTranslateRemoveEntityEvent() throws SerializationDataMissing {
    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    UUID target = UUID.randomUUID();

    RemoveEntityOutgoingEventType outgoing =
        EventTypeFactory.createRemoveEntityOutgoingEvent(target, chunkRange);
    RemoveEntityIncomingEventType incoming =
        NetworkDataDeserializer.createRemoveEntityIncomingEventType(
            NetworkDataSerializer.createRemoveEntityOutgoingEventType(outgoing));

    assert outgoing.getTarget().equals(incoming.getTarget());
    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
  }
}
