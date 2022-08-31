package core.chunk;

import static core.common.CommonFactory.createCoordinates;

import core.common.ChunkRange;
import core.common.CommonFactory;
import java.util.List;
import org.junit.Test;

public class testChunkRange {
  @Test
  public void testHashEqual() {
    ChunkRange chunkRange1 = CommonFactory.createChunkRange(createCoordinates(0, 0));
    ChunkRange chunkRange2 = CommonFactory.createChunkRange(createCoordinates(0, 0));
    System.out.println(chunkRange1);
    System.out.println(chunkRange2);
    assert chunkRange1.equals(chunkRange2);
  }

  @Test
  public void testRelative() {
    ChunkRange chunkRange1 = CommonFactory.createChunkRange(createCoordinates(0, 0));
    assert chunkRange1.getLeft().equals(CommonFactory.createChunkRange(createCoordinates(-1, 0)));
    assert chunkRange1
        .getRight()
        .equals(CommonFactory.createChunkRange(createCoordinates(ChunkRange.size, 0)));
    assert chunkRange1
        .getDown()
        .equals(CommonFactory.createChunkRange(createCoordinates(0, -ChunkRange.size)));
    assert chunkRange1
        .getUp()
        .equals(CommonFactory.createChunkRange(createCoordinates(0, ChunkRange.size)));
  }

  @Test
  public void getChunkRangeListTwoPoints() {

    ChunkRange root = CommonFactory.createChunkRange(createCoordinates(0, 0));
    ChunkRange rightUpRoot = root.getRight().getUp();

    List<ChunkRange> chunkRangeList =
        ChunkRange.getChunkRangeListTwoPoints(
            createCoordinates(0, 0), createCoordinates(rightUpRoot.bottom_x, rightUpRoot.bottom_y));

    assert chunkRangeList.contains(root);
    assert chunkRangeList.contains(root.getUp());
    assert chunkRangeList.contains(root.getRight());
    assert chunkRangeList.contains(root.getRight().getUp());
    assert chunkRangeList.size() == 4;

    rightUpRoot = root.getRight().getUp().getRight().getUp();

    chunkRangeList =
        ChunkRange.getChunkRangeListTwoPoints(
            createCoordinates(0, 0), createCoordinates(rightUpRoot.bottom_x, rightUpRoot.bottom_y));

    assert chunkRangeList.size() == 9;

    assert ChunkRange.getChunkRangeListTwoPoints(createCoordinates(0, 0), createCoordinates(1, 1))
            .size()
        == 1;

    assert ChunkRange.getChunkRangeListTwoPoints(createCoordinates(0, 0), createCoordinates(0, 0))
            .size()
        == 1;

    assert ChunkRange.getChunkRangeListTwoPoints(createCoordinates(-1, 0), createCoordinates(0, 0))
            .size()
        == 2;

    assert ChunkRange.getChunkRangeListTwoPoints(createCoordinates(-1, -1), createCoordinates(0, 0))
            .size()
        == 4;
  }

  @Test
  public void testGetChunkRangeListAroundPoint() {
    assert ChunkRange.getChunkRangeListAroundPoint(createCoordinates(0, 0), 1).size() == 9;

    assert ChunkRange.getChunkRangeListAroundPoint(createCoordinates(0, 0), 2).size() == 25;
  }

  @Test
  public void testGetChunkRangeNegativeFloat() {

    ChunkRange chunkRange = CommonFactory.createChunkRange(createCoordinates(-0.1f, 0));

    assert !chunkRange.equals(CommonFactory.createChunkRange(createCoordinates(0, 0)));
  }
}
