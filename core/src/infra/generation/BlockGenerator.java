package infra.generation;

import com.google.inject.Inject;
import infra.common.networkobject.Coordinates;
import infra.entity.block.Block;
import infra.entity.block.BlockFactory;

public class BlockGenerator {

    @Inject
    BlockFactory blockFactory;
    public Block generate(Coordinates coordinates){
        Block block;
        if(coordinates.getY()> 0){
            block = blockFactory.createSky();
        }
        else if (Math.random() < 0.1){
            block = blockFactory.createStone();
        }
        else{
            block = blockFactory.createDirt();
        }

        block.coordinates = coordinates;
        return block;
    }
}
