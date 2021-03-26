package infra.map;

import com.google.inject.Inject;
import infra.common.Coordinate;
import infra.map.block.Block;
import infra.map.block.BlockFactory;

import java.util.List;

public class WorldMap {
    MapGrid mapGrid;
    BlockFactory blockFactory;

    @Inject
    WorldMap(BlockFactory blockFactory) {
        this.blockFactory = blockFactory;
        mapGrid = new MapGrid();
    }

    void generateArea(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Coordinate genCoordinate = new Coordinate(x + i, y + i);
                if (this.mapGrid.getBlock(genCoordinate) == null) {
                    continue;
                }
                Block newBlock = blockFactory.createBlock(genCoordinate);
                this.mapGrid.addBlock(newBlock);
            }
        }
    }

    List<Block> getBlocksInRange(int x, int y, int width, int height) {
        return this.mapGrid.getBlocksInRange(x, y, width, height);
    }
}
