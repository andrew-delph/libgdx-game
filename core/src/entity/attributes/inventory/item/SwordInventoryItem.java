package entity.attributes.inventory.item;

import networking.NetworkObjects.NetworkData;
import networking.translation.NetworkDataSerializer;

public class SwordInventoryItem extends AbstractInventoryItem {

  public SwordInventoryItem(int index) {
    super(index);
  }

  @Override
  public void activate() {}

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.serializeSwordInventoryItem(this);
  }
}
