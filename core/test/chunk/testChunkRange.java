package chunk;

import common.Coordinates;
import org.junit.Test;

import java.util.List;

public class testChunkRange {
  @Test
  public void testHashEqual() {
    ChunkRange chunkRange1 = new ChunkRange(new Coordinates(0, 0));
    ChunkRange chunkRange2 = new ChunkRange(new Coordinates(0, 0));
    System.out.println(chunkRange1);
    System.out.println(chunkRange2);
    assert chunkRange1.equals(chunkRange2);
  }

  @Test
  public void testRelative() {
    ChunkRange chunkRange1 = new ChunkRange(new Coordinates(0, 0));
    assert chunkRange1.getLeft().equals(new ChunkRange(new Coordinates(-1, 0)));
    assert chunkRange1.getRight().equals(new ChunkRange(new Coordinates(ChunkRange.size, 0)));
    assert chunkRange1.getDown().equals(new ChunkRange(new Coordinates(0, -ChunkRange.size)));
    assert chunkRange1.getUp().equals(new ChunkRange(new Coordinates(0, ChunkRange.size)));
  }

  @Test
  public void getChunkRangeListTwoPoints() {

    ChunkRange root = new ChunkRange(new Coordinates(0, 0));
    ChunkRange rightUpRoot = root.getRight().getUp();

    List<ChunkRange> chunkRangeList =
        ChunkRange.getChunkRangeListTwoPoints(
            new Coordinates(0, 0), new Coordinates(rightUpRoot.bottom_x, rightUpRoot.bottom_y));

    assert chunkRangeList.size() == 4;

    rightUpRoot = root.getRight().getUp().getRight().getUp();

    chunkRangeList =
        ChunkRange.getChunkRangeListTwoPoints(
            new Coordinates(0, 0), new Coordinates(rightUpRoot.bottom_x, rightUpRoot.bottom_y));

    assert chunkRangeList.size() == 9;
  }

  @Test
  public void testGetChunkRangeListAroundPoint() {
    assert ChunkRange.getChunkRangeListAroundPoint(new Coordinates(0, 0), 1).size() == 9;

    assert ChunkRange.getChunkRangeListAroundPoint(new Coordinates(0, 0), 2).size() == 25;
  }

  @Test
  public void testGetChunkRangeNegativeFloat() {

    ChunkRange chunkRange = new ChunkRange(new Coordinates(-0.1f, 0));

    assert !chunkRange.equals(new ChunkRange(new Coordinates(0, 0)));
  }
}
