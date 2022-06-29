package entity.attributes.inventory.item;

import entity.attributes.Attribute;
import entity.attributes.AttributeType;
import java.util.Objects;

public abstract class AbstractInventoryItem implements Attribute {
  int index;

  public AbstractInventoryItem(int index) {
    this.index = index;
  }

  public ItemActionType getItemActionType() {
    return ItemActionType.DEFAULT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractInventoryItem that = (AbstractInventoryItem) o;
    return index == that.index;
  }

  @Override
  public int hashCode() {
    return Objects.hash(index);
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  @Override
  public AttributeType getType() {
    return AttributeType.ITEM;
  }
}
