package infra.map.chunk;

import infra.common.Coordinate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestChunkRange {
    @Test
    public void chunk() {

        ChunkRange chunkRange1 = new ChunkRange(new Coordinate(152,66));
        System.out.println(chunkRange1.bottom_x+","+chunkRange1.bottom_y+","+chunkRange1.top_x+","+chunkRange1.top_y);

        ChunkRange chunkRange2 = new ChunkRange(new Coordinate(152+10,66+7));
        System.out.println(chunkRange2.bottom_x+","+chunkRange2.bottom_y+","+chunkRange2.top_x+","+chunkRange2.top_y);

        assertEquals(chunkRange1,chunkRange2);
    }
}
