package infra.map;

import base.BaseCamera;
import com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import generation.MapBuilder;
import infra.common.Coordinate;
import infra.map.block.Block;
import infra.map.block.BlockFactory;
import infra.map.chunk.Chunk;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

    public void cameraGenerateArea(BaseCamera camera) {
        List<Chunk> chunkList = getChunksOnCamera(camera);
        for (Chunk chunk : chunkList) {
            if (!chunk.generated) {
                mapBuilder.generateWorld(chunk);
            }
        }
    }

    public List<Block> cameraGetBlocks(BaseCamera camera) {
        List<Chunk> chunkList = getChunksOnCamera(camera);
        List<Block> blockList = new LinkedList();
        for (Chunk chunk : chunkList) {
            blockList.addAll(chunk.getBlocks());
        }
        return blockList;
    }

    public Coordinate cameraPositionToCoordinate(BaseCamera camera) {
        Coordinate coordinate = new Coordinate(camera.position.x / size, camera.position.y / size);
        System.out.println(coordinate.getX() + "," + coordinate.getY());
        return coordinate;
    }

    public List<Chunk> getChunksOnCamera(BaseCamera camera) {
        Set<Chunk> chunkSet = new HashSet<>();
        Coordinate cameraCord = cameraPositionToCoordinate(camera);
//        chunkSet.add(this.mapGrid.getChunk(new Coordinate(camera.position.x/size-camera.viewportWidth/size,camera.position.y/size)));
        chunkSet.add(this.mapGrid.getChunk(cameraCord));

        for (int x = (int) (cameraCord.getX() - camera.viewportWidth / size / 2); x < cameraCord.getX() + camera.viewportWidth / size / 2; x++) {
            for (int y = (int) (cameraCord.getY() - camera.viewportHeight / size / 2); y < cameraCord.getY() + camera.viewportHeight / size / 2; y++) {
                Coordinate diffCoord = new Coordinate(x, y);
                chunkSet.add(this.mapGrid.getChunk(diffCoord));
            }
        }

        return Lists.newArrayList(chunkSet);
    }
}
