package core.networking.translation;

import core.common.ChunkRange;
import core.common.CommonFactory;
import core.common.exceptions.SerializationDataMissing;
import core.common.Coordinates;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.RemoveEntityIncomingEventType;
import core.networking.events.types.outgoing.RemoveEntityOutgoingEventType;
import java.util.UUID;
import org.junit.Test;

public class TranslateRemoveEntityEvent {

  @Test
  public void testTranslateRemoveEntityEvent() throws SerializationDataMissing {
    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    ChunkRange chunkRange = CommonFactory.createChunkRange(coordinates);
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
