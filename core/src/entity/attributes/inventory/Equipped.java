package entity.attributes.inventory;

import entity.attributes.Attribute;
import entity.attributes.AttributeType;
import networking.NetworkObjects.NetworkData;

public class Equipped implements Attribute {
  private int index;
  private int size;

  public Equipped(int index, int size) {
    this.index = index;
    this.size = size;
  }

  public int getSize() {
    return size;
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
    return Math.min(index + 1, size);
  }

  @Override
  public AttributeType getType() {
    return null;
  }

  @Override
  public NetworkData toNetworkData() {
    return null;
  }
}
