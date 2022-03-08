package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import networking.ConnectionStore;
import networking.client.ClientNetworkHandle;
import networking.events.types.incoming.AuthenticationIncomingEventType;

import java.util.function.Consumer;

public class AuthenticationIncomingConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Inject ConnectionStore connectionStore;

  @Override
  public void accept(EventType eventType) {
    AuthenticationIncomingEventType incoming = (AuthenticationIncomingEventType) eventType;
    connectionStore.addConnection(incoming.getUser(), incoming.getRequestNetworkEventObserver());
    clientNetworkHandle.authLatch.countDown();
  }
}
