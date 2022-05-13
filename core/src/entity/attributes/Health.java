package entity.attributes;

import com.google.common.base.Objects;
import networking.NetworkObjects.NetworkData;

public class Health implements Attribute {

  float health;

  public Health(float health) {
    this.health = health;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Health health1 = (Health) o;
    return Float.compare(health1.health, health) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(health);
  }

  @Override
  public NetworkData toNetworkData() {
    return null;
  }

  @Override
  public AttributeType getType() {
    return AttributeType.HEALTH;
  }
}
