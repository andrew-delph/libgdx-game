package core.networking.events.consumer.server.incoming;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.events.types.incoming.PingRequestIncomingEventType;
import core.networking.translation.NetworkDataSerializer;
import java.util.function.Consumer;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.outgoing.PingResponseOutgoingEventType;
import core.networking.server.ServerNetworkHandle;

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
