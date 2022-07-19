package core.networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.events.types.outgoing.HandshakeOutgoingEventType;
import java.util.function.Consumer;
import core.networking.client.ClientNetworkHandle;

public class HandshakeOutgoingConsumerClient implements Consumer<EventType> {
  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    HandshakeOutgoingEventType realEvent = (HandshakeOutgoingEventType) eventType;
    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}
