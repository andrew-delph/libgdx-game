package core.entity.attributes.inventory.item;

import core.networking.translation.NetworkDataSerializer;
import networking.NetworkObjects.NetworkData;

public class SwordInventoryItem extends AbstractInventoryItem {

  public SwordInventoryItem(int index) {
    super(index);
  }

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.serializeSwordInventoryItem(this);
  }
}
