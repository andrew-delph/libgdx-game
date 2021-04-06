package infra.map;

import base.BaseCamera;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import infra.common.Coordinate;
import infra.map.block.Block;
import infra.map.block.BlockFactory;
import infra.map.chunk.Chunk;
import infra.map.chunk.MapBuilder;

import java.util.List;

public class WorldMap {
    public MapGrid mapGrid;
    BlockFactory blockFactory;

    @Inject
    MapBuilder mapBuilder;

    @Inject
    @Named("CoordinateScale")
    int size;

    @Inject
    WorldMap(BlockFactory blockFactory) {
        this.blockFactory = blockFactory;
        mapGrid = new MapGrid();
    }

    public void generateArea(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Coordinate genCoordinate = new Coordinate(x + i, y + j);
//                System.out.println("new"+(x + i)+","+);

                if (this.mapGrid.getBlock(genCoordinate) != null) {
                    continue;
                }
                Block newBlock = blockFactory.createBlock(genCoordinate);
                this.mapGrid.addBlock(newBlock);
            }
        }
    }

    public List<Block> getBlocksInRange(int x, int y, int width, int height) {
        return this.mapGrid.getBlocksInRange(x, y, width, height);
    }

    public void cameraGenerateArea(BaseCamera camera){
        Chunk genChunk  = this.mapGrid.getChunk(cameraPositionToCoordinate(camera));
        if (!genChunk.generated){
            mapBuilder.generateDirt(genChunk);
        }
    }

    public List<Block> cameraGetBlocks(BaseCamera camera){
        Chunk genChunk  = this.mapGrid.getChunk(cameraPositionToCoordinate(camera));
        return genChunk.getBlocks();
    }

    public Coordinate cameraPositionToCoordinate(BaseCamera camera){

        Coordinate coordinate = new Coordinate(camera.position.x/size,camera.position.y/size);
        System.out.println(coordinate.getX()+","+coordinate.getY());
        return coordinate;
    }
}
