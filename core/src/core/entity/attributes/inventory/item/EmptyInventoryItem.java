package core.entity.attributes.inventory.item;

import core.networking.translation.NetworkDataSerializer;
import networking.NetworkObjects.NetworkData;

public class EmptyInventoryItem extends AbstractInventoryItem {

  public EmptyInventoryItem(int index) {
    super(index);
  }

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.serializeEmptyInventoryItem(this);
  }
}
