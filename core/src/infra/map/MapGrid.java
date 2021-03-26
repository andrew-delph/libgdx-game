package infra.map;

import infra.common.Coordinate;
import infra.map.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapGrid {
    Map<Coordinate, Block> blockMap;

    public MapGrid() {
        this.blockMap = new HashMap<>();
    }

    void addBlock(Block block) {
        this.blockMap.put(block.coordinate, block);
    }

    Block getBlock(Coordinate coordinate) {
        return this.blockMap.get(coordinate);
    }

    List<Block> getBlocksInRange(int x, int y, int width, int height) {
        ArrayList<Block> blocksList = new ArrayList();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Coordinate getCoordinate = new Coordinate(x + i, y + j);
                blocksList.add(this.blockMap.get(getCoordinate));
            }
        }
        return blocksList;
    }
}
