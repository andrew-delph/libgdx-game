package infra.entity;

import com.google.inject.Inject;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.block.BlockFactory;
import infra.entity.block.DirtBlock;
import infra.entity.block.SkyBlock;
import infra.entity.block.StoneBlock;
import infra.networking.NetworkObjects;

import java.util.UUID;

public class EntitySerializationConverter {
  @Inject EntityFactory entityFactory;
  @Inject BlockFactory blockFactory;

  @Inject GameStore gameStore;

  public Entity createEntity(NetworkObjects.NetworkData networkData) {
    String classString = networkData.getValue();
    Entity entity;
    if (classString.equals(DirtBlock.class.getName())) {
      entity = blockFactory.createDirt();
    } else if (classString.equals(SkyBlock.class.getName())) {
      entity = blockFactory.createSky();
    } else if (classString.equals(StoneBlock.class.getName())) {
      entity = blockFactory.createStone();
    } else {
      entity = entityFactory.createEntity();
    }
    for (NetworkObjects.NetworkData networkDataChild : networkData.getChildrenList()) {
      if (networkDataChild.getKey().equals(Coordinates.class.getName())) {
        entity.coordinates = this.createCoordinates(networkDataChild);
      } else if (networkDataChild.getKey().equals(UUID.class.getName())) {
        entity.uuid = UUID.fromString(networkDataChild.getValue());
      }
    }
    return entity;
  }

  public Coordinates createCoordinates(NetworkObjects.NetworkData networkData) {
    float x = 0, y = 0;
    for (NetworkObjects.NetworkData value : networkData.getChildrenList()) {
      switch (value.getKey()) {
        case "x":
          x = Float.parseFloat(value.getValue());
          break;
        case "y":
          y = Float.parseFloat(value.getValue());
          break;
      }
    }
    return new Coordinates(x, y);
  }

  public Entity updateEntity(NetworkObjects.NetworkData networkData) {
    Coordinates coordinates = null;
    UUID uuid = null;
    for (NetworkObjects.NetworkData networkDataChild : networkData.getChildrenList()) {
      if (networkDataChild.getKey().equals(Coordinates.class.getName())) {
        coordinates = this.createCoordinates(networkDataChild);
      } else if (networkDataChild.getKey().equals(UUID.class.getName())) {
        uuid = UUID.fromString(networkDataChild.getValue());
      }
    }
    if (uuid != null) {
      Entity entity = this.gameStore.getEntity(uuid);
      entity.coordinates = coordinates;
      return entity;
    }
    return null;
  }
}
