package infra.common.networkobject;

import infra.serialization.SerializationItem;
import networking.NetworkObject;

import java.util.UUID;

public class Owner implements SerializationItem {

  UUID uuid;

  Owner(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public NetworkObject getNetworkObject() {
    return null;
  }
}
