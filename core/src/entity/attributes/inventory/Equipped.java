package entity.attributes.inventory;

import com.google.common.base.Objects;
import entity.attributes.Attribute;
import entity.attributes.AttributeType;
import networking.NetworkObjects.NetworkData;
import networking.translation.NetworkDataSerializer;

public class Equipped implements Attribute {
  private int index;

  public Equipped(int index) {
    this.index = index;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Equipped equipped = (Equipped) o;
    return index == equipped.index;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(index);
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public int getLeftIndex() {
    return Math.max(index - 1, 0);
  }

  public int getRightIndex() {
    return Math.min(index + 1, InventoryBag.size);
  }

  @Override
  public AttributeType getType() {
    return AttributeType.EQUIPPED;
  }

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.createEquipped(this);
  }
}
