package generation;

import com.google.inject.Inject;
import generation.layer.AbstractLayer;
import generation.layer.fill.DirtFillLayer;
import generation.layer.random.StoneRandomLayer;
import generation.noise.FastNoiseGenerator;
import infra.map.block.BlockFactory;
import infra.map.chunk.Chunk;

import java.util.LinkedList;
import java.util.List;

public class MapBuilder {

    FastNoiseGenerator noise;

    List<AbstractLayer> layerList;

//    @Inject
//    BlockFactory blockFactory;

    @Inject
    public MapBuilder(StoneRandomLayer stoneRandomLayer, DirtFillLayer dirtFillLayer) {
        noise = new FastNoiseGenerator();
        noise.setTopRange(2);
        noise.setyScale(7);
        noise.setxScale(7);
        layerList = new LinkedList<>();
        layerList.add(dirtFillLayer);
        layerList.add(stoneRandomLayer);
    }

    public void generateWorld(Chunk chunk) {
        for (AbstractLayer abstractLayer : layerList) {
            System.out.println(abstractLayer);
            abstractLayer.generateLayer(chunk);
        }
        chunk.generated = true;
    }

//    public void generateWorld(Chunk chunk){
//        ChunkRange chunkRange = chunk.chunkRange;
//        for (int i =chunkRange.bottom_x; i < chunkRange.top_x; i++) {
//            for (int j = chunkRange.bottom_y; j <chunkRange.top_y;j++){
//                int noise_value = noise.getValue(i,j);
//                if(noise_value==0){
//                    chunk.addBlock(blockFactory.createBlock(new Coordinate(i,j)));
//                }
//                else{
//                    chunk.addBlock(blockFactory.createDirtBlock(new Coordinate(i,j)));
//                }
//            }
//        }
//        chunk.generated = true;
//    }
}
