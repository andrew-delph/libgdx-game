package entity.attributes.inventory;

import org.junit.Test;

public class testInventoryBag {

  @Test
  public void test() throws FullBagException {
    int size = 10;
    InventoryBag bag = new InventoryBag(size);

    assert bag.freeSpace() == size;

    for (int i = 0; i < size; i++) {
      bag.appendItem(new OrbInventoryItem(bag.getNextFreeIndex()));
    }

    boolean error = false;

    try {
      bag.appendItem(new OrbInventoryItem(bag.getNextFreeIndex()));
    } catch (FullBagException e) {
      error = true;
    }

    assert error;

    bag.updateItem(new EmptyInventoryItem(3));

    assert bag.freeSpace() == 1;

    assert bag.getNextFreeIndex() == 3;
  }
}
