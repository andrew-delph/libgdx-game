package infra.map.chunk;

import infra.common.Coordinate;
import infra.map.block.Block;

import java.util.Map;

public class Chunk {
    Boolean generated = false;
    static final int size = 50;
    Map<Coordinate, Block> blockMap;
    public Coordinate coordinate;
    public Chunk(Coordinate coordinate){
        this.coordinate = coordinate;
    }
}
