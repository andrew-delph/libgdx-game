package entity.attributes.inventory.item;

import networking.NetworkObjects.NetworkData;
import networking.translation.NetworkDataSerializer;

public class EmptyInventoryItem extends AbstractInventoryItem {

  public EmptyInventoryItem(int index) {
    super(index);
  }

  @Override
  public void activate() {}

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.serializeEmptyInventoryItem(this);
  }
}
