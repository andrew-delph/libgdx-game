package entity.attributes.inventory;

import entity.attributes.inventory.item.AbstractInventoryItem;
import entity.attributes.inventory.item.EmptyInventoryItem;
import java.util.Arrays;
import java.util.Objects;

public class InventoryBag {
  final AbstractInventoryItem[] inventoryItemList;
  final int size;
  private Equipped equipped;

  public InventoryBag(int size) {
    this.size = size;
    equipped = new Equipped(0, size);
    inventoryItemList = new AbstractInventoryItem[size];
    for (int i = 0; i < size; i++) {
      inventoryItemList[i] = new EmptyInventoryItem(i);
    }
  }

  public void setEquipped(Equipped equipped) {
    this.equipped = equipped;
  }

  public AbstractInventoryItem getEquippedItem() {
    return this.getItem(equipped.getIndex());
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
    item.setIndex(getNextFreeIndex());
    this.updateItem(item);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryBag that = (InventoryBag) o;
    return size == that.size && Arrays.equals(inventoryItemList, that.inventoryItemList);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(size);
    result = 31 * result + Arrays.hashCode(inventoryItemList);
    return result;
  }

  public synchronized void updateItem(AbstractInventoryItem item) {
    int index = item.getIndex();
    inventoryItemList[index] = item;
  }

  public synchronized void removeItem(int index) {
    inventoryItemList[index] = new EmptyInventoryItem(index);
  }

  public synchronized AbstractInventoryItem getItem(Integer index) {
    return inventoryItemList[index];
  }

  public synchronized int getNextFreeIndex() throws FullBagException {
    try {
      return getClassIndex(EmptyInventoryItem.class);
    } catch (ItemNotFoundException e) {
      throw new FullBagException();
    }
  }

  public synchronized int getClassIndex(Class<? extends AbstractInventoryItem> itemClass)
      throws ItemNotFoundException {
    for (AbstractInventoryItem item : inventoryItemList) {

      if (item.getClass().isAssignableFrom(itemClass)) return item.getIndex();
    }
    throw new ItemNotFoundException();
  }

  public synchronized AbstractInventoryItem[] getItemList() {
    return Arrays.copyOf(this.inventoryItemList, inventoryItemList.length);
  }
}
