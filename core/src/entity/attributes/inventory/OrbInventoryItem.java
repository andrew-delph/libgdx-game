package entity.attributes.inventory;

import entity.attributes.AttributeType;
import networking.NetworkObjects.NetworkData;

public class OrbInventoryItem extends AbstractInventoryItem {

  public OrbInventoryItem(int index) {
    super(index);
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
