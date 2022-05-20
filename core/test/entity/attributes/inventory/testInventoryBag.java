package entity.attributes.inventory;

import entity.attributes.inventory.item.EmptyInventoryItem;
import entity.attributes.inventory.item.OrbInventoryItem;
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

  @Test
  public void testEqual1() {

    int size = 10;

    InventoryBag b1 = new InventoryBag(size);

    InventoryBag b2 = new InventoryBag(size);

    assert b1.equals(b2);
  }

  @Test
  public void testEqual2() {

    int size = 10;

    InventoryBag b1 = new InventoryBag(size);

    b1.updateItem(new OrbInventoryItem(2));

    InventoryBag b2 = new InventoryBag(size);

    assert !b1.equals(b2);

    b2.updateItem(new OrbInventoryItem(2));

    assert b1.equals(b2);
  }
}
