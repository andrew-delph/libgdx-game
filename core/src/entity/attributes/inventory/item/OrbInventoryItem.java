package entity.attributes.inventory.item;

import networking.NetworkObjects.NetworkData;
import networking.translation.NetworkDataSerializer;

public class OrbInventoryItem extends AbstractInventoryItem {

  public OrbInventoryItem(int index) {
    super(index);
  }

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.serializeOrbInventoryItem(this);
  }
}
