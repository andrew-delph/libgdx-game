package infra.map.block;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.CoreApp;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class TestBlockFactory {
    @Test
    public void checkSize() {
        Injector injector = Guice.createInjector(new CoreApp());
        BlockFactory factory = injector.getInstance(BlockFactory.class);
        Block testBlock = factory.createBlock(0,0);
        assertEquals(testBlock.size, 15);
    }
}
