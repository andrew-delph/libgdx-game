package infra.chunk;

import infra.common.Coordinates;
import infra.networking.NetworkObjects;

public class ChunkRange {
  public static final int size = 2;
  public int bottom_x;
  public int bottom_y;
  public int top_x;
  public int top_y;

  public ChunkRange(Coordinates coordinates) {
    if (coordinates.getX() < 0) {
      this.bottom_x = ((((coordinates.getX() + 1) / size)) * size) - size;
    } else {
      this.bottom_x = ((coordinates.getX() / size)) * size;
    }

    if (coordinates.getY() < 0) {
      this.bottom_y = ((((coordinates.getY() + 1) / size)) * size) - size;
    } else {
      this.bottom_y = ((coordinates.getY() / size)) * size;
    }

    this.top_y = this.bottom_y + size;
    this.top_x = this.bottom_x + size;
  }

  public synchronized ChunkRange getUp() {
    return new ChunkRange(new Coordinates(this.bottom_x, this.top_y + 1));
  }

  public synchronized ChunkRange getDown() {
    return new ChunkRange(new Coordinates(this.bottom_x, this.bottom_y - 1));
  }

  public synchronized ChunkRange getLeft() {
    return new ChunkRange(new Coordinates(this.bottom_x - 1, this.bottom_y));
  }

  public synchronized ChunkRange getRight() {
    return new ChunkRange(new Coordinates(this.top_x + 1, this.bottom_y));
  }

  @Override
  public int hashCode() {
    return (this.bottom_x + "," + this.bottom_x + "," + this.top_x + "," + this.top_y).hashCode();
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

  public NetworkObjects.NetworkData toNetworkData() {
    NetworkObjects.NetworkData.Builder builder = NetworkObjects.NetworkData.newBuilder().setKey(this.getClass().getName());
    NetworkObjects.NetworkData x = NetworkObjects.NetworkData.newBuilder().setKey("x").setValue(String.valueOf(this.bottom_x)).build();
    NetworkObjects.NetworkData y = NetworkObjects.NetworkData.newBuilder().setKey("x").setValue(String.valueOf(this.bottom_x)).build();
    return  builder.addChildren(x).addChildren(y).build();
  }

}
