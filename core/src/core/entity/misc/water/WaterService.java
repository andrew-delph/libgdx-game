package core.entity.misc.water;

import core.common.Coordinates;
import java.util.HashSet;
import java.util.Set;

public class WaterService {

  Set<Coordinates> waterSet = new HashSet<>();

  public void init() {}

  public void reset() {
    waterSet.clear();
  }

  public void registerPosition(Coordinates coordinates) {
    waterSet.add(coordinates);
  }

  public boolean hasPosition(Coordinates coordinates) {
    return waterSet.contains(coordinates);
  }
}
