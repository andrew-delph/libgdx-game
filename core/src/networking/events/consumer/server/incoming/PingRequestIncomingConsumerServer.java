package networking.events.consumer.server.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.PingRequestIncomingEventType;
import networking.events.types.outgoing.PingResponseOutgoingEventType;
import networking.server.ServerNetworkHandle;
import networking.translation.NetworkDataSerializer;

public class PingRequestIncomingConsumerServer implements Consumer<EventType> {

  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    PingRequestIncomingEventType realEvent = (PingRequestIncomingEventType) eventType;

    PingResponseOutgoingEventType pingResponseOutgoingEventType =
        EventTypeFactory.createPingResponseOutgoingEventType(realEvent.getPingID());

    serverNetworkHandle.send(
        realEvent.getUserID(),
        NetworkDataSerializer.createPingResponseOutgoingEventType(pingResponseOutgoingEventType));
  }
}
