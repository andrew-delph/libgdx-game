package networking.events.interfaces;

import networking.NetworkObjects;

public interface SerializeNetworkEvent {
  NetworkObjects.NetworkEvent toNetworkEvent();
}
