package networking.events.consumer.server.incoming;

import static networking.translation.DataTranslationEnum.AUTH;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.ConnectionStore;
import networking.NetworkObjects;
import networking.events.types.incoming.AuthenticationIncomingEventType;
import networking.server.ServerNetworkHandle;

public class AuthenticationIncomingConsumerServer implements Consumer<EventType> {

  @Inject ServerNetworkHandle serverNetworkHandle;

  @Inject ConnectionStore connectionStore;

  @Override
  public void accept(EventType eventType) {
    AuthenticationIncomingEventType incoming = (AuthenticationIncomingEventType) eventType;

    connectionStore.addConnection(incoming.getUser(), incoming.getRequestNetworkEventObserver());

    NetworkObjects.NetworkEvent authenticationEvent =
        NetworkObjects.NetworkEvent.newBuilder().setEvent(AUTH).build();

    serverNetworkHandle.send(incoming.getUser(), authenticationEvent);
  }
}
