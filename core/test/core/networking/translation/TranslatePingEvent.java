package core.networking.translation;

import core.app.user.UserID;
import core.common.exceptions.SerializationDataMissing;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.PingRequestIncomingEventType;
import core.networking.events.types.incoming.PingResponseIncomingEventType;
import core.networking.events.types.outgoing.PingRequestOutgoingEventType;
import core.networking.events.types.outgoing.PingResponseOutgoingEventType;
import java.util.UUID;
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
