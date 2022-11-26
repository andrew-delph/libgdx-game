package core.networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.networking.ConnectionStore;
import core.networking.client.ClientNetworkHandle;
import core.networking.events.types.incoming.AuthenticationIncomingEventType;
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
