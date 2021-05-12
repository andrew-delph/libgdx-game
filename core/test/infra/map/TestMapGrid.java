package infra.map;

import infra.common.Coordinate;
import infra.map.block.Block;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestMapGrid {
  @Test
  public void test() {
    MapGrid mapGrid = new MapGrid();

    assertNull(mapGrid.getBlock(null));

    Block testBlock = new Block(new Coordinate(15, 15), 15);

    mapGrid.addBlock(testBlock);

    assertEquals(testBlock, mapGrid.getBlock(new Coordinate(15, 15)));

    assertNull(mapGrid.getBlock(new Coordinate(15, 77)));
  }
}
