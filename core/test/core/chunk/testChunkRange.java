package core.chunk;

import core.common.CommonFactory;
import java.util.List;
import org.junit.Test;

public class testChunkRange {
  @Test
  public void testHashEqual() {
    ChunkRange chunkRange1 = new ChunkRange(CommonFactory.createCoordinates(0, 0));
    ChunkRange chunkRange2 = new ChunkRange(CommonFactory.createCoordinates(0, 0));
    System.out.println(chunkRange1);
    System.out.println(chunkRange2);
    assert chunkRange1.equals(chunkRange2);
  }

  @Test
  public void testRelative() {
    ChunkRange chunkRange1 = new ChunkRange(CommonFactory.createCoordinates(0, 0));
    assert chunkRange1.getLeft().equals(new ChunkRange(CommonFactory.createCoordinates(-1, 0)));
    assert chunkRange1
        .getRight()
        .equals(new ChunkRange(CommonFactory.createCoordinates(ChunkRange.size, 0)));
    assert chunkRange1
        .getDown()
        .equals(new ChunkRange(CommonFactory.createCoordinates(0, -ChunkRange.size)));
    assert chunkRange1
        .getUp()
        .equals(new ChunkRange(CommonFactory.createCoordinates(0, ChunkRange.size)));
  }

  @Test
  public void getChunkRangeListTwoPoints() {

    ChunkRange root = new ChunkRange(CommonFactory.createCoordinates(0, 0));
    ChunkRange rightUpRoot = root.getRight().getUp();

    List<ChunkRange> chunkRangeList =
        ChunkRange.getChunkRangeListTwoPoints(
            CommonFactory.createCoordinates(0, 0),
            CommonFactory.createCoordinates(rightUpRoot.bottom_x, rightUpRoot.bottom_y));

    assert chunkRangeList.contains(root);
    assert chunkRangeList.contains(root.getUp());
    assert chunkRangeList.contains(root.getRight());
    assert chunkRangeList.contains(root.getRight().getUp());
    assert chunkRangeList.size() == 4;

    rightUpRoot = root.getRight().getUp().getRight().getUp();

    chunkRangeList =
        ChunkRange.getChunkRangeListTwoPoints(
            CommonFactory.createCoordinates(0, 0),
            CommonFactory.createCoordinates(rightUpRoot.bottom_x, rightUpRoot.bottom_y));

    assert chunkRangeList.size() == 9;
  }

  @Test
  public void testGetChunkRangeListAroundPoint() {
    assert ChunkRange.getChunkRangeListAroundPoint(CommonFactory.createCoordinates(0, 0), 1).size()
        == 9;

    assert ChunkRange.getChunkRangeListAroundPoint(CommonFactory.createCoordinates(0, 0), 2).size()
        == 25;
  }

  @Test
  public void testGetChunkRangeNegativeFloat() {

    ChunkRange chunkRange = new ChunkRange(CommonFactory.createCoordinates(-0.1f, 0));

    assert !chunkRange.equals(new ChunkRange(CommonFactory.createCoordinates(0, 0)));
  }
}
