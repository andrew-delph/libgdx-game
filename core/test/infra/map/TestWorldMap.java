package infra.map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.CoreApp;
import configure.TestApp;
import infra.map.block.Block;
import infra.map.block.BlockFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestWorldMap {

    @Test
    public void testGenerateAndGet() {
        Injector injector = Guice.createInjector(new TestApp());
        WorldMap worldMap = injector.getInstance(WorldMap.class);
        worldMap.generateArea(0,0,15,20);
        List<Block> blocks = worldMap.getBlocksInRange(0,0,15,20);
        for (Block block : blocks) {
            assertNotNull(block);
        }
    }
}
