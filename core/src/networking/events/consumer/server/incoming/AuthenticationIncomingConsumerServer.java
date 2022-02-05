package networking.events.consumer.server.incoming;

import com.google.inject.Inject;
import common.events.types.EventType;
import networking.ConnectionStore;
import networking.NetworkObjects;
import networking.events.types.incoming.AuthenticationIncomingEventType;
import networking.server.ServerNetworkHandle;

import java.util.function.Consumer;

import static networking.translation.DataTranslationEnum.AUTH;

public class AuthenticationIncomingConsumerServer implements Consumer<EventType> {

    @Inject
    ServerNetworkHandle serverNetworkHandle;

    @Inject
    ConnectionStore connectionStore;

    @Override
    public void accept(EventType eventType) {
        AuthenticationIncomingEventType incoming = (AuthenticationIncomingEventType) eventType;

        connectionStore.addConnection(incoming.getUser(), incoming.getRequestNetworkEventObserver());

        NetworkObjects.NetworkEvent authenticationEvent =
                NetworkObjects.NetworkEvent.newBuilder().setEvent(AUTH).build();

        serverNetworkHandle.send(incoming.getUser(), authenticationEvent);
    }
}
