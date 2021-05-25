package infra.common.networkobject;

import infra.networking.NetworkObjects;
import infra.serialization.SerializationData;

import java.util.UUID;

public class Owner implements SerializationData {

  UUID uuid;

  Owner(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public NetworkObjects.NetworkData toNetworkData() {
    return null;
  }
}
