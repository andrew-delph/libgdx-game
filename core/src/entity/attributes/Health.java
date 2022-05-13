package entity.attributes;

import networking.NetworkObjects.NetworkData;
import networking.events.interfaces.SerializeNetworkData;

public class Health implements SerializeNetworkData {

  float health;

  public Health(float health) {
    this.health = health;
  }

  @Override
  public NetworkData toNetworkData() {
    return null;
  }
}
