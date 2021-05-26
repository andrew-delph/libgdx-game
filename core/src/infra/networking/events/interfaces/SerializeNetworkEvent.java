package infra.networking.events.interfaces;

import infra.networking.NetworkObjects;

public interface SerializeNetworkEvent {
    NetworkObjects.NetworkEvent toNetworkEvent();
}
