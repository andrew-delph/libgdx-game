package entity.attributes;

import networking.NetworkObjects.NetworkData;

public class Health implements Attribute {

  float health;

  public Health(float health) {
    this.health = health;
  }

  @Override
  public NetworkData toNetworkData() {
    return null;
  }
}
