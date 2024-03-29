package core.common;

import static core.common.CommonFactory.createChunkRange;
import static core.common.CommonFactory.createCoordinates;

import core.networking.events.interfaces.SerializeNetworkData;
import core.networking.translation.NetworkDataSerializer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import networking.NetworkObjects;

public class ChunkRange implements SerializeNetworkData {
  public static final int size = GameSettings.CHUNK_SIZE;
  public final int bottom_x;
  public final int bottom_y;
  public final int top_x;
  public final int top_y;

  protected ChunkRange(Coordinates coordinates) {
    if (coordinates.getXReal() < 0) {
      this.bottom_x = ((((coordinates.getX() + 1) / size)) * size) - size;
    } else {
      this.bottom_x = ((coordinates.getX() / size)) * size;
    }

    if (coordinates.getYReal() < 0) {
      this.bottom_y = ((((coordinates.getY() + 1) / size)) * size) - size;
    } else {
      this.bottom_y = ((coordinates.getY() / size)) * size;
    }

    this.top_y = this.bottom_y + size;
    this.top_x = this.bottom_x + size;
  }

  public static List<ChunkRange> getChunkRangeListTwoPoints(
      Coordinates bottomLeftCoordinates, Coordinates topRightCoordinates) {

    ChunkRange bottomLeftChunkRange = createChunkRange(bottomLeftCoordinates);
    ChunkRange topRightChunkRange = createChunkRange(topRightCoordinates);

    ChunkRange topLeftChunkRange =
        createChunkRange(
            createCoordinates(bottomLeftChunkRange.bottom_x, topRightChunkRange.bottom_y));
    //    ChunkRange bottomRightChunkRange = CommonFactory.createChunkRange(new
    // Coordinates(topRightChunkRange.bottom_x,bottomLeftChunkRange.bottom_y));

    List<ChunkRange> chunkRangeList = new LinkedList<>();

    ChunkRange root = bottomLeftChunkRange;
    ChunkRange current;
    while (!root.equals(topLeftChunkRange.getUp())) {
      current = root;
      root = root.getUp();
      ChunkRange rowRightChunkRange =
          createChunkRange(createCoordinates(topRightChunkRange.bottom_x, current.bottom_y));
      while (!current.equals(rowRightChunkRange.getRight())) {
        chunkRangeList.add(current);
        current = current.getRight();
      }
    }
    return chunkRangeList;
  }

  public static List<ChunkRange> getChunkRangeListAroundPoint(
      Coordinates coordinates, int chunkRangeRadius) {
    Set<ChunkRange> chunkRangeSet = new HashSet<>();

    ChunkRange bottomLeftChunkRange = createChunkRange(coordinates);
    ChunkRange topRightChunkRange = createChunkRange(coordinates);

    for (int i = 0; i < chunkRangeRadius; i++) {
      bottomLeftChunkRange = bottomLeftChunkRange.getLeft().getDown();
      topRightChunkRange = topRightChunkRange.getRight().getUp();
    }

    return ChunkRange.getChunkRangeListTwoPoints(
        createCoordinates(bottomLeftChunkRange.bottom_x, bottomLeftChunkRange.bottom_y),
        createCoordinates(topRightChunkRange.bottom_x, topRightChunkRange.bottom_y));
  }

  public NetworkObjects.NetworkData toNetworkData() {
    return NetworkDataSerializer.createChunkRange(this);
  }

  public synchronized ChunkRange getUp() {
    return createChunkRange(createCoordinates(this.bottom_x, this.top_y + 1));
  }

  public synchronized ChunkRange getDown() {
    return createChunkRange(createCoordinates(this.bottom_x, this.bottom_y - 1));
  }

  public synchronized ChunkRange getLeft() {
    return createChunkRange(createCoordinates(this.bottom_x - 1, this.bottom_y));
  }

  public synchronized ChunkRange getRight() {
    return createChunkRange(createCoordinates(this.top_x + 1, this.bottom_y));
  }

  @Override
  public int hashCode() {
    return (this.bottom_x + "," + this.bottom_y + "," + this.top_x + "," + this.top_y).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ChunkRange other = (ChunkRange) obj;
    return bottom_x == other.bottom_x
        && bottom_y == other.bottom_y
        && top_x == other.top_x
        && top_y == other.top_y;
  }

  public String toString() {
    return this.bottom_x + "," + this.bottom_y + "," + this.top_x + "," + this.top_y;
  }
}
