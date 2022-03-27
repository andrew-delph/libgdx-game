package networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.client.ClientNetworkHandle;
import networking.events.types.outgoing.PingRequestOutgoingEventType;
import networking.translation.NetworkDataSerializer;

public class PingRequestOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    PingRequestOutgoingEventType realEvent = (PingRequestOutgoingEventType) eventType;

    clientNetworkHandle.send(NetworkDataSerializer.createPingRequestOutgoingEventType(realEvent));
  }
}
