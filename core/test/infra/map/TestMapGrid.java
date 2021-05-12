package infra.map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.ClientTestApp;
import infra.common.Coordinate;
import infra.map.block.Block;
import infra.map.block.BlockFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestMapGrid {
  @Test
  public void test() {
    MapGrid mapGrid = new MapGrid();

    assertNull(mapGrid.getBlock(null));

    Injector injector = Guice.createInjector(new ClientTestApp());;

    injector.getInstance(BlockFactory.class).createBlock(new Coordinate(15, 15));

    Block testBlock = injector.getInstance(BlockFactory.class).createBlock(new Coordinate(15, 15));

    mapGrid.addBlock(testBlock);

    assertEquals(testBlock, mapGrid.getBlock(new Coordinate(15, 15)));

    assertNull(mapGrid.getBlock(new Coordinate(15, 77)));
  }
}
