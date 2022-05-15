package entity.attributes.inventory;

import java.util.LinkedList;
import java.util.List;

public class InventoryBag {
  int size;
  List<AbstractInventoryItem> inventoryItemList = new LinkedList<>();

  public InventoryBag(int size) {
    this.size = size;
    for (int i = 0; i < size; i++) {
      inventoryItemList.add(new EmptyInventoryItem(i)); // TODO add empty item
    }
  }

  public int freeSpace() {
    return 0;
  }
}
