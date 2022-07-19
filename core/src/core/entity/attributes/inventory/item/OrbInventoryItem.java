package core.entity.attributes.inventory.item;

import core.networking.translation.NetworkDataSerializer;
import networking.NetworkObjects.NetworkData;

public class OrbInventoryItem extends AbstractInventoryItem {

  public OrbInventoryItem(int index) {
    super(index);
  }

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.serializeOrbInventoryItem(this);
  }
}
