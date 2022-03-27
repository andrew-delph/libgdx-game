package networking.translation;

import app.user.UserID;
import common.exceptions.SerializationDataMissing;
import java.util.UUID;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.PingRequestIncomingEventType;
import networking.events.types.incoming.PingResponseIncomingEventType;
import networking.events.types.outgoing.PingRequestOutgoingEventType;
import networking.events.types.outgoing.PingResponseOutgoingEventType;
import org.junit.Test;

public class TranslatePingEvent {

  @Test
  public void testTranslatePingRequest() throws SerializationDataMissing {
    PingRequestOutgoingEventType requestOutgoingEventType =
        EventTypeFactory.createPingRequestOutgoingEventType(
            UserID.createUserID(), UUID.randomUUID());

    PingRequestIncomingEventType requestIncomingEventType =
        NetworkDataDeserializer.createPingRequestIncomingEventType(
            NetworkDataSerializer.createPingRequestOutgoingEventType(requestOutgoingEventType));

    assert requestOutgoingEventType.getPingID().equals(requestIncomingEventType.getPingID());
  }

  @Test
  public void testTranslatePingResponse() throws SerializationDataMissing {
    PingResponseOutgoingEventType responseOutgoingEventType =
        EventTypeFactory.createPingResponseOutgoingEventType(UUID.randomUUID());

    PingResponseIncomingEventType pingResponseIncomingEventType =
        NetworkDataDeserializer.createPingResponseIncomingEventType(
            NetworkDataSerializer.createPingResponseOutgoingEventType(responseOutgoingEventType));

    assert responseOutgoingEventType.getPingID().equals(pingResponseIncomingEventType.getPingID());
  }
}
