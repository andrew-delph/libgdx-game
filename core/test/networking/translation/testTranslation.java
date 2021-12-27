package networking.translation;

import chunk.ChunkRange;
import common.Coordinates;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.HandshakeIncomingEventType;
import networking.events.types.outgoing.HandshakeOutgoingEventType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class testTranslation {

    @Test
    public void testUUID() {
        UUID uuidExpected = UUID.randomUUID();
        UUID uuidRetrieved = NetworkDataDeserializer.createUUID(NetworkDataSerializer.createUUID(uuidExpected));
        assert uuidExpected.equals(uuidRetrieved);
    }

    @Test
    public void testUUIDList() {
        List<UUID> expectedList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            expectedList.add(UUID.randomUUID());
        }
        List<UUID> retrievedList = NetworkDataDeserializer.createUUIDList(NetworkDataSerializer.createUUIDList(expectedList));
        Assert.assertEquals(expectedList, retrievedList);
    }

    @Test
    public void testHandshake() {
        ChunkRange chunkRange = new ChunkRange(new Coordinates(-1, -1));
        List<UUID> uuidList = new ArrayList<>();
        uuidList.add(UUID.randomUUID());
        uuidList.add(UUID.randomUUID());
        uuidList.add(UUID.randomUUID());
        uuidList.add(UUID.randomUUID());
        uuidList.add(UUID.randomUUID());

        HandshakeOutgoingEventType handshakeOutgoing = EventTypeFactory.createHandshakeOutgoingEventType(chunkRange, uuidList);

        HandshakeIncomingEventType handshakeIncoming = NetworkDataDeserializer.createHandshakeIncomingEventType(handshakeOutgoing.toNetworkEvent());

        assert handshakeOutgoing.getChunkRange().equals(handshakeIncoming.getChunkRange());
        Assert.assertEquals(handshakeIncoming.getListUUID(), handshakeOutgoing.getListUUID());
    }
}
