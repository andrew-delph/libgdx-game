package core.networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.translation.NetworkDataSerializer;
import java.util.function.Consumer;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.types.outgoing.PingRequestOutgoingEventType;

public class PingRequestOutgoingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    PingRequestOutgoingEventType realEvent = (PingRequestOutgoingEventType) eventType;

    clientNetworkHandle.send(NetworkDataSerializer.createPingRequestOutgoingEventType(realEvent));
  }
}
