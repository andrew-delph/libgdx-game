package infra.serialization;

import infra.networking.NetworkObjects;

public interface SerializationEvent {

    NetworkObjects.NetworkEvent toNetworkEvent();
}
