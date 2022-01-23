package networking.translation;

import chunk.ChunkRange;
import common.Coordinates;
import common.exceptions.SerializationDataMissing;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.ChunkSwapIncomingEventType;
import networking.events.types.outgoing.ChunkSwapOutgoingEventType;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class TranslateChunkSwapEvent {

    @Test
    public void testTranslateChunkSwapEvent() throws SerializationDataMissing {
        ChunkRange from = new ChunkRange(new Coordinates(0, 0));
        ChunkRange to = new ChunkRange(new Coordinates(-1, 0));
        UUID target = UUID.randomUUID();

        ChunkSwapOutgoingEventType outgoing = EventTypeFactory.createChunkSwapOutgoingEventType(target, from, to);
        ChunkSwapIncomingEventType incoming = NetworkDataDeserializer.createChunkSwapIncomingEventType(
                NetworkDataSerializer.createChunkSwapOutgoingEventType(outgoing));

        assert incoming.getTarget().equals(outgoing.getTarget());
        Assert.assertEquals(incoming.getFrom(), outgoing.getFrom());
        Assert.assertEquals(incoming.getTo(), outgoing.getTo());
    }

}
