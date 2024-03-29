package core.entity.attributes.msc;

import com.google.common.base.Objects;
import core.entity.attributes.Attribute;
import core.entity.attributes.AttributeType;
import core.networking.translation.NetworkDataSerializer;
import networking.NetworkObjects.NetworkData;

public class Health implements Attribute {

  float health;

  public Health(float health) {
    this.health = health;
  }

  @Override
  public String toString() {
    return "Health{" + "health=" + health + '}';
  }

  public Health applyDiff(float diff) {
    return new Health(this.health + diff);
  }

  public float getValue() {
    return health;
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
    return NetworkDataSerializer.createHealth(this);
  }

  @Override
  public AttributeType getType() {
    return AttributeType.HEALTH;
  }

  public Boolean isAlive() {
    return this.health > 0;
  }
}
