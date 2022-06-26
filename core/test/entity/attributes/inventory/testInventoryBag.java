package entity.attributes.inventory;

import entity.attributes.inventory.item.EmptyInventoryItem;
import entity.attributes.inventory.item.OrbInventoryItem;
import org.junit.Test;

public class testInventoryBag {

  @Test
  public void test() throws FullBagException {
    InventoryBag bag = new InventoryBag();

    assert bag.freeSpace() == InventoryBag.size;

    for (int i = 0; i < InventoryBag.size; i++) {
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

    InventoryBag b1 = new InventoryBag();

    InventoryBag b2 = new InventoryBag();

    assert b1.equals(b2);
  }

  @Test
  public void testEqual2() {

    InventoryBag b1 = new InventoryBag();

    b1.updateItem(new OrbInventoryItem(2));

    InventoryBag b2 = new InventoryBag();

    assert !b1.equals(b2);

    b2.updateItem(new OrbInventoryItem(2));

    assert b1.equals(b2);
  }

  @Test
  public void testGetClassIndex() throws ItemNotFoundException {

    InventoryBag b1 = new InventoryBag();

    b1.updateItem(new OrbInventoryItem(3));

    assert 3 == b1.getClassIndex(OrbInventoryItem.class);
  }

  @Test(expected = ItemNotFoundException.class)
  public void testGetClassIndexFail() throws ItemNotFoundException {

    InventoryBag b1 = new InventoryBag();
    b1.getClassIndex(OrbInventoryItem.class);
  }
}
