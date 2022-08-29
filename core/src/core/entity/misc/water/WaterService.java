package core.entity.misc.water;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.Coordinates;
import core.common.exceptions.ChunkNotFound;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WaterService {

  Map<Coordinates, UUID> waterMap = new HashMap<>();
  Set<Coordinates> currSet = new HashSet<>();

  @Inject GameController gameController;

  public void notifyPosition(Coordinates coordinates) {
    currSet.add(coordinates.getBase());
  }

  public void update() throws ChunkNotFound {
    // get coordinates that dont have water for them
    // get coordinates that need to have water

    Set<Coordinates> waterToRemove = (new HashSet<>(waterMap.keySet()));
    waterToRemove.removeAll(currSet);

    Set<Coordinates> waterToAdd = (new HashSet<>(currSet));
    waterToAdd.removeAll(waterMap.keySet());

    for (Coordinates toAdd : waterToAdd) {
      Water w = gameController.createWater(toAdd);
      waterMap.put(toAdd, w.getUuid());
    }

    for (Coordinates toRemove : waterToRemove) {
      gameController.removeEntity(waterMap.remove(toRemove));
    }

    currSet = new HashSet<>();
  }
}
