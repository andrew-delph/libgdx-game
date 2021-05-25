package infra.serialization;

import infra.networking.NetworkObjects;

public interface SerializationData {

  NetworkObjects.NetworkData toNetworkData();
}
