package infra.entity.pathfinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.block.*;
import org.junit.Test;

public class testBlockStructure {
    @Test
    public void testRelativeBlockRegister(){
        Injector injector = Guice.createInjector(new SoloConfig());

        GameStore gameStore = injector.getInstance(GameStore.class);
        BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
        BlockStructureFactory blockStructureFactory = injector.getInstance(BlockStructureFactory.class);

        ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

        gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0,0))));

        Block block = blockFactory.createSky();
        block.coordinates = new Coordinates(0,0);
        gameStore.addEntity(block);

        assert gameStore.getBlock(new Coordinates(0,0)) != null;

        BlockStructure blockStructure = blockStructureFactory.createBlockStructure();
        blockStructure.registerRelativeBlock(new RelativeCoordinates(0,0), EmptyBlock.class);

        assert blockStructure.verifyBlockStructure(new Coordinates(0,0));
    }

    @Test
    public void testRelativeBlockRegisterNegative(){
        Injector injector = Guice.createInjector(new SoloConfig());

        GameStore gameStore = injector.getInstance(GameStore.class);
        BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
        BlockStructureFactory blockStructureFactory = injector.getInstance(BlockStructureFactory.class);

        ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);

        gameStore.addChunk(chunkFactory.create(new ChunkRange(new Coordinates(0,0))));

        Block block = blockFactory.createSky();
        block.coordinates = new Coordinates(0,0);
        gameStore.addEntity(block);

        assert gameStore.getBlock(new Coordinates(0,0)) != null;

        BlockStructure blockStructure = blockStructureFactory.createBlockStructure();
        blockStructure.registerRelativeBlock(new RelativeCoordinates(0,0), SolidBlock.class);

        assert !blockStructure.verifyBlockStructure(new Coordinates(0,0));
    }
}
