package core.networking.translation;

import core.chunk.ChunkRange;
import core.common.exceptions.SerializationDataMissing;
import core.entity.attributes.msc.Coordinates;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.RemoveEntityIncomingEventType;
import core.networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import java.util.UUID;
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
