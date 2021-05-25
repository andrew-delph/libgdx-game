package infra.common.networkobject;

import infra.networking.NetworkObjects;
import infra.serialization.SerializationData;

import java.util.UUID;

public class ID implements SerializationData {

  UUID uuid;

  public ID(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public NetworkObjects.NetworkData toNetworkData() {
    return null;
  }
}
