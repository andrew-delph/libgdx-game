package infra.map.block;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import infra.common.Coordinate;

public class BlockFactory {

    int size = 10;

    @Inject
    BlockFactory(@Named("CoordinateScale") int size){
        this.size = size;
    }

    public Block createBlock(int x, int y){
        return new Block(new Coordinate(x,y), this.size);
    }
    public Block createBlock(Coordinate coordinate){
        return new Block(coordinate, this.size);
    }
}
