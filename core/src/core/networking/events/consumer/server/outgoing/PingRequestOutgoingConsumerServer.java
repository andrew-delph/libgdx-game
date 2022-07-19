package core.networking.events.consumer.server.outgoing;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.events.types.outgoing.PingRequestOutgoingEventType;
import core.networking.translation.NetworkDataSerializer;
import java.util.function.Consumer;
import core.networking.server.ServerNetworkHandle;

public class PingRequestOutgoingConsumerServer implements Consumer<EventType> {

  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    PingRequestOutgoingEventType realEvent = (PingRequestOutgoingEventType) eventType;

    serverNetworkHandle.send(
        realEvent.getUserID(), NetworkDataSerializer.createPingRequestOutgoingEventType(realEvent));
  }
}
