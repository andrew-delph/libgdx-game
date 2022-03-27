package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.PingRequestIncomingEventType;
import networking.events.types.outgoing.PingResponseOutgoingEventType;
import networking.translation.NetworkDataSerializer;

public class PingRequestIncomingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    PingRequestIncomingEventType realEvent = (PingRequestIncomingEventType) eventType;

    PingResponseOutgoingEventType pingResponseOutgoingEventType =
        EventTypeFactory.createPingResponseOutgoingEventType(realEvent.getPingID());

    clientNetworkHandle.send(
        NetworkDataSerializer.createPingResponseOutgoingEventType(pingResponseOutgoingEventType));
  }
}
