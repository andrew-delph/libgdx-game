package entity.pathfinding;

import common.Coordinates;
import entity.Entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PathGameStoreOverride {
    Map<Coordinates, List<Class<? extends Entity>>> gameStoreOverride = new HashMap<>();

    public PathGameStoreOverride(PathGameStoreOverride oldPathGameStoreOverride) {
        gameStoreOverride = oldPathGameStoreOverride.copyMap();
    }

    public PathGameStoreOverride() {
        gameStoreOverride = new HashMap<>();
    }

    public Map<Coordinates, List<Class<? extends Entity>>> getGameStoreOverride() {
        return gameStoreOverride;
    }

    public Map<Coordinates, List<Class<? extends Entity>>> copyMap() {
        Map<Coordinates, List<Class<? extends Entity>>> copy = new HashMap<>();
        for (Map.Entry<Coordinates, List<Class<? extends Entity>>> entryMap :
                this.gameStoreOverride.entrySet()) {
            copy.put(entryMap.getKey(), new LinkedList<>(entryMap.getValue()));
        }
        return copy;
    }

    public List<Class<? extends Entity>> getEntityListBaseCoordinates(Coordinates coordinates) {
        coordinates = coordinates.getBase();
        return gameStoreOverride.get(coordinates);
    }

    public void registerEntityTypeOverride(
            Class<? extends Entity> classType, Coordinates coordinates) {
        this.gameStoreOverride.computeIfAbsent(coordinates, k -> new LinkedList<>());
        this.gameStoreOverride.get(coordinates).add(classType);
    }
}
