package core.networking.events.consumer.server.incoming;

import static core.networking.translation.DataTranslationEnum.AUTH;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import java.util.function.Consumer;
import core.networking.ConnectionStore;
import networking.NetworkObjects;
import core.networking.events.types.incoming.AuthenticationIncomingEventType;
import core.networking.server.ServerNetworkHandle;

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
