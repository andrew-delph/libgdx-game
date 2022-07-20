package core.networking.translation;

import core.chunk.ChunkRange;
import core.common.CommonFactory;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.HandshakeIncomingEventType;
import core.networking.events.types.outgoing.HandshakeOutgoingEventType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class testTranslation {

  @Test
  public void testUUID() {
    UUID uuidExpected = UUID.randomUUID();
    UUID uuidRetrieved =
        NetworkDataDeserializer.createUUID(NetworkDataSerializer.createUUID(uuidExpected));
    assert uuidExpected.equals(uuidRetrieved);
  }

  @Test
  public void testUUIDList() {
    List<UUID> expectedList = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      expectedList.add(UUID.randomUUID());
    }
    List<UUID> retrievedList =
        NetworkDataDeserializer.createUUIDList(NetworkDataSerializer.createUUIDList(expectedList));
    Assert.assertEquals(expectedList, retrievedList);
  }

  @Test
  public void testHandshake() {
    ChunkRange chunkRange = CommonFactory.createChunkRange(CommonFactory.createCoordinates(-1, -1));
    List<UUID> uuidList = new ArrayList<>();
    uuidList.add(UUID.randomUUID());
    uuidList.add(UUID.randomUUID());
    uuidList.add(UUID.randomUUID());
    uuidList.add(UUID.randomUUID());
    uuidList.add(UUID.randomUUID());

    HandshakeOutgoingEventType handshakeOutgoing =
        EventTypeFactory.createHandshakeOutgoingEventType(chunkRange, uuidList);

    HandshakeIncomingEventType handshakeIncoming =
        NetworkDataDeserializer.createHandshakeIncomingEventType(
            handshakeOutgoing.toNetworkEvent());

    assert handshakeOutgoing.getChunkRange().equals(handshakeIncoming.getChunkRange());
    Assert.assertEquals(handshakeIncoming.getListUUID(), handshakeOutgoing.getListUUID());
  }
}
