package generation.layer.random.tunnel;

import infra.common.Coordinate;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Tunnel {

  public Set<Coordinate> coordinateSet;
  int seed;
  int length = 50;
  Random generator;
  Coordinate lastCoordinate;

  public Tunnel(int seed, Coordinate startingCoordinate) {
    this.seed = seed;
    this.lastCoordinate = startingCoordinate;
    this.generator = new Random(this.seed);
    this.coordinateSet = new HashSet<>();
    this.coordinateSet.add(startingCoordinate);
  }

  public static void main(String args[]) {
    Tunnel tunnel = new Tunnel(1, null);
    for (int i = 0; i < 25; i++) {
      tunnel.next();
    }
  }

  Set<Coordinate> generate() {
    for (int i = 0; i < this.length; i++) {
      Coordinate newCoordinate = this.next();
      this.coordinateSet.add(newCoordinate);
      this.lastCoordinate = newCoordinate;
    }
    return this.coordinateSet;
  }

  Coordinate next() {
    int direction = generator.nextInt(4);
    if (direction == 0) {
      return this.lastCoordinate.getUp();
    } else if (direction == 1) {
      return this.lastCoordinate.getDown();
    } else if (direction == 2) {
      return this.lastCoordinate.getLeft();
    } else {
      return this.lastCoordinate.getRight();
    }
  }
}
