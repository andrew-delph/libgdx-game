package networking.events.consumer.server.outgoing;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.events.types.outgoing.PingRequestOutgoingEventType;
import networking.server.ServerNetworkHandle;
import networking.translation.NetworkDataSerializer;

public class PingRequestOutgoingConsumerServer implements Consumer<EventType> {

  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    PingRequestOutgoingEventType realEvent = (PingRequestOutgoingEventType) eventType;

    serverNetworkHandle.send(
        realEvent.getUserID(), NetworkDataSerializer.createPingRequestOutgoingEventType(realEvent));
  }
}
