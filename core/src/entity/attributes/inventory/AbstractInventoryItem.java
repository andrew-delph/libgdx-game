package entity.attributes.inventory;

import entity.attributes.Attribute;

public abstract class AbstractInventoryItem implements Attribute {
  int index;

  public AbstractInventoryItem(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }
}
