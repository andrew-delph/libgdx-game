package entity.attributes.inventory;

public class InventoryBag {
  final AbstractInventoryItem[] inventoryItemList;
  int size;

  public InventoryBag(int size) {
    this.size = size;
    inventoryItemList = new AbstractInventoryItem[size];
    for (int i = 0; i < size; i++) {
      inventoryItemList[i] = new EmptyInventoryItem(i);
    }
  }

  public synchronized int freeSpace() {
    int freeSpace = 0;
    for (AbstractInventoryItem item : inventoryItemList) {
      if (item instanceof EmptyInventoryItem) freeSpace++;
    }
    return freeSpace;
  }

  public synchronized void appendItem(AbstractInventoryItem item) throws FullBagException {
    if (freeSpace() == 0) throw new FullBagException();
    inventoryItemList[getNextFreeIndex()] = item;
  }

  public synchronized void updateItem(AbstractInventoryItem item) throws FullBagException {
    int index = item.getIndex();
    inventoryItemList[index] = item;
  }

  public synchronized int getNextFreeIndex() throws FullBagException {
    for (AbstractInventoryItem item : inventoryItemList) {
      if (item instanceof EmptyInventoryItem) return item.getIndex();
    }
    throw new FullBagException();
  }
}
