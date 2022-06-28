package networking.translation;

import static networking.translation.DataTranslationEnum.COORDINATES;

import app.user.UserID;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import com.sun.tools.javac.util.Pair;
import common.GameStore;
import common.events.types.CreateAIEntityEventType;
import common.events.types.CreateTurretEventType;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.Attribute;
import entity.attributes.inventory.Equipped;
import entity.attributes.inventory.item.EmptyInventoryItem;
import entity.attributes.inventory.item.OrbInventoryItem;
import entity.attributes.inventory.item.SwordInventoryItem;
import entity.attributes.msc.Coordinates;
import entity.attributes.msc.Health;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.DirtBlock;
import entity.block.SkyBlock;
import entity.block.StoneBlock;
import entity.misc.Ladder;
import entity.misc.Orb;
import entity.misc.Projectile;
import entity.misc.Turret;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import networking.NetworkObjects;
import networking.NetworkObjects.NetworkData;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.ChunkSwapIncomingEventType;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.incoming.HandshakeIncomingEventType;
import networking.events.types.incoming.PingRequestIncomingEventType;
import networking.events.types.incoming.PingResponseIncomingEventType;
import networking.events.types.incoming.RemoveEntityIncomingEventType;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.incoming.UpdateEntityIncomingEventType;

public class NetworkDataDeserializer {
  @Inject EntityFactory entityFactory;
  @Inject BlockFactory blockFactory;
  @Inject ChunkFactory chunkFactory;
  @Inject GameStore gameStore;

  public static ChunkRange createChunkRange(NetworkObjects.NetworkData networkData) {
    // TODO put in translations
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
    return new ChunkRange(new Coordinates(x, y));
  }

  public static UUID createUUID(NetworkObjects.NetworkData networkData) {
    return UUID.fromString(networkData.getValue());
  }

  public static Health createHealth(NetworkObjects.NetworkData networkData) {
    return new Health(Float.parseFloat(networkData.getValue()));
  }

  public static Equipped createEquipped(NetworkObjects.NetworkData networkData) {
    return new Equipped(Integer.parseInt(networkData.getValue()));
  }

  public static List<UUID> createUUIDList(NetworkObjects.NetworkData networkData) {
    List<UUID> uuidList = new LinkedList<>();
    for (NetworkObjects.NetworkData child : networkData.getChildrenList()) {
      if (child.getKey().equals(DataTranslationEnum.UUID)) {
        uuidList.add(createUUID(child));
      }
    }
    return uuidList;
  }

  public static HandshakeIncomingEventType createHandshakeIncomingEventType(
      NetworkObjects.NetworkEvent networkEvent) {
    ChunkRange chunkRange = null;
    List<UUID> uuidList = null;
    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      switch (child.getKey()) {
        case DataTranslationEnum.UUID_LIST:
          uuidList = createUUIDList(child);
          break;
        case DataTranslationEnum.CHUNK_RANGE:
          chunkRange = createChunkRange(child);
          break;
      }
    }
    UserID connectionUserID = null;
    if (!networkEvent.getUser().isEmpty()) {
      connectionUserID = UserID.createUserID(networkEvent.getUser());
    }
    return EventTypeFactory.createHandshakeIncomingEventType(
        connectionUserID, chunkRange, uuidList);
  }

  public static UpdateEntityIncomingEventType createUpdateEntityIncomingEvent(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    List<Attribute> attributeList = new LinkedList<>();
    UUID uuid = null;
    UserID user = null;
    ChunkRange chunkRange = null;

    if (!networkEvent.getUser().isEmpty()) {
      user = UserID.createUserID(networkEvent.getUser());
    }

    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      if (DataTranslationEnum.CHUNK_RANGE.equals(child.getKey())) {
        chunkRange = createChunkRange(child);
      } else if (DataTranslationEnum.UUID.equals(child.getKey())) {
        uuid = createUUID(child);
      } else {
        attributeList.add(createAttribute(child));
        // it will be another attribute
      }
    }
    if (chunkRange == null) throw new SerializationDataMissing("Missing chunkRange");
    if (uuid == null) throw new SerializationDataMissing("Missing uuid");

    return EventTypeFactory.createUpdateEntityIncomingEvent(user, attributeList, chunkRange, uuid);
  }

  public static Attribute createAttribute(NetworkData networkData) throws SerializationDataMissing {
    if (DataTranslationEnum.COORDINATES.equals(networkData.getKey())) {
      return createCoordinates(networkData);
    } else if (DataTranslationEnum.HEALTH.equals(networkData.getKey())) {
      return createHealth(networkData);
    } else if (DataTranslationEnum.EMPTY_ITEM.equals(networkData.getKey())) {
      return createEmptyItem(networkData);
    } else if (DataTranslationEnum.ORB_ITEM.equals(networkData.getKey())) {
      return createOrbItem(networkData);
    } else if (DataTranslationEnum.SWORD_ITEM.equals(networkData.getKey())) {
      return createSwordItem(networkData);
    } else if (DataTranslationEnum.EQUIPPED.equals(networkData.getKey())) {
      return createEquipped(networkData);
    }
    return null;
  }

  public static EmptyInventoryItem createEmptyItem(NetworkData networkData)
      throws SerializationDataMissing {
    Integer index = null;
    for (NetworkObjects.NetworkData child : networkData.getChildrenList()) {
      if (DataTranslationEnum.INDEX.equals(child.getKey())) {
        index = Integer.valueOf(child.getValue());
      }
    }
    if (index == null) throw new SerializationDataMissing("Missing index");
    return new EmptyInventoryItem(index);
  }

  public static OrbInventoryItem createOrbItem(NetworkData networkData)
      throws SerializationDataMissing {
    Integer index = null;
    for (NetworkObjects.NetworkData child : networkData.getChildrenList()) {
      if (DataTranslationEnum.INDEX.equals(child.getKey())) {
        index = Integer.valueOf(child.getValue());
      }
    }
    if (index == null) throw new SerializationDataMissing("Missing index");
    return new OrbInventoryItem(index);
  }

  public static SwordInventoryItem createSwordItem(NetworkData networkData)
      throws SerializationDataMissing {
    Integer index = null;
    for (NetworkObjects.NetworkData child : networkData.getChildrenList()) {
      if (DataTranslationEnum.INDEX.equals(child.getKey())) {
        index = Integer.valueOf(child.getValue());
      }
    }
    if (index == null) throw new SerializationDataMissing("Missing index");
    return new SwordInventoryItem(index);
  }

  public static CreateEntityIncomingEventType createCreateEntityIncomingEventType(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    UserID userID = null;
    ChunkRange chunkRange = null;
    NetworkObjects.NetworkData networkData = null;

    if (!networkEvent.getUser().isEmpty()) {
      userID = UserID.createUserID(networkEvent.getUser());
    }

    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      if (DataTranslationEnum.CHUNK_RANGE.equals(child.getKey())) {
        chunkRange = createChunkRange(child);
      } else {
        networkData = child;
      }
    }
    if (chunkRange == null) throw new SerializationDataMissing("Missing chunkRange");
    if (networkData == null) throw new SerializationDataMissing("Missing networkData");

    return EventTypeFactory.createCreateEntityIncomingEvent(userID, networkData, chunkRange);
  }

  public static RemoveEntityIncomingEventType createRemoveEntityIncomingEventType(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    UserID user = null;
    ChunkRange chunkRange = null;
    UUID target = null;
    if (!networkEvent.getUser().isEmpty()) {
      user = UserID.createUserID(networkEvent.getUser());
    }
    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      switch (child.getKey()) {
        case DataTranslationEnum.UUID:
          target = createUUID(child);
          break;
        case DataTranslationEnum.CHUNK_RANGE:
          chunkRange = createChunkRange(child);
          break;
      }
    }
    if (chunkRange == null) throw new SerializationDataMissing("Missing chunkRange");
    if (target == null) throw new SerializationDataMissing("Missing target");
    return EventTypeFactory.createRemoveEntityIncomingEvent(user, chunkRange, target);
  }

  public static Coordinates createCoordinates(NetworkObjects.NetworkData networkData) {
    // TODO put in translations
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

  public static CreateAIEntityEventType createCreateAIEntityEventType(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    UUID target = null;
    Coordinates coordinates = null;

    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      switch (child.getKey()) {
        case DataTranslationEnum.UUID:
          target = createUUID(child);
          break;
        case COORDINATES:
          coordinates = createCoordinates(child);
          break;
      }
    }
    if (target == null) throw new SerializationDataMissing("Missing target uuid");
    if (coordinates == null) throw new SerializationDataMissing("Missing coordinates");

    return EventTypeFactory.createAIEntityEventType(coordinates, target);
  }

  public static ChunkSwapIncomingEventType createChunkSwapIncomingEventType(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    UUID target = null;
    ChunkRange from = null;
    ChunkRange to = null;

    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      switch (child.getKey()) {
        case DataTranslationEnum.UUID:
          target = createUUID(child);
          break;
        case "from":
          from = createChunkRange(child);
          break;
        case "to":
          to = createChunkRange(child);
          break;
      }
    }
    if (target == null) throw new SerializationDataMissing("Missing target uuid");
    if (from == null) throw new SerializationDataMissing("Missing from");
    if (to == null) throw new SerializationDataMissing("Missing to");

    return EventTypeFactory.createChunkSwapIncomingEventType(target, from, to);
  }

  public static PingRequestIncomingEventType createPingRequestIncomingEventType(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    UUID pingID = null;
    UserID user = null;

    if (!networkEvent.getUser().isEmpty()) {
      user = UserID.createUserID(networkEvent.getUser());
    }

    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      switch (child.getKey()) {
        case DataTranslationEnum.UUID:
          pingID = createUUID(child);
          break;
      }
    }

    if (pingID == null) throw new SerializationDataMissing("Missing pingID");
    return EventTypeFactory.createPingRequestIncomingEventType(user, pingID);
  }

  public static PingResponseIncomingEventType createPingResponseIncomingEventType(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    UUID pingID = null;
    UserID user = null;
    Long time = null;

    if (!networkEvent.getUser().isEmpty()) {
      user = UserID.createUserID(networkEvent.getUser());
    }
    if (networkEvent.getTime() > 0) {
      time = networkEvent.getTime();
    }

    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      switch (child.getKey()) {
        case DataTranslationEnum.UUID:
          pingID = createUUID(child);
          break;
      }
    }

    if (pingID == null) throw new SerializationDataMissing("Missing pingID");
    return EventTypeFactory.createPingResponseIncomingEventType(user, pingID, time);
  }

  public static CreateTurretEventType createCreateTurretEventType(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    Coordinates coordinates = null;
    UUID uuid = null;

    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      switch (child.getKey()) {
        case COORDINATES:
          coordinates = createCoordinates(child);
          break;
        case DataTranslationEnum.UUID:
          uuid = createUUID(child);
          break;
      }
    }
    if (coordinates == null) throw new SerializationDataMissing("Missing coordinates");
    if (uuid == null) throw new SerializationDataMissing("Missing uuid");

    return EventTypeFactory.createTurretEventType(uuid, coordinates);
  }

  public Chunk createChunk(NetworkObjects.NetworkData networkData) throws SerializationDataMissing {
    Pair<ChunkRange, List<Entity>> chunkData = this.createChunkData(networkData);
    Chunk chunkToCreate = chunkFactory.create(chunkData.fst);
    chunkToCreate.addAllEntity(chunkData.snd);
    return chunkToCreate;
  }

  public Pair<ChunkRange, List<Entity>> createChunkData(NetworkObjects.NetworkData networkData)
      throws SerializationDataMissing {
    List<Entity> entityList = new LinkedList<>();
    ChunkRange chunkRange = null;
    for (NetworkObjects.NetworkData networkDataChild : networkData.getChildrenList()) {
      String classString = networkDataChild.getKey();

      if (classString.equals(DataTranslationEnum.CHUNK_RANGE)) {
        chunkRange = createChunkRange(networkDataChild);
      } else {
        entityList.add(this.createEntity(networkDataChild));
      }
    }
    return new Pair<>(chunkRange, entityList);
  }

  public Entity createEntity(NetworkObjects.NetworkData networkData)
      throws SerializationDataMissing {
    String classString = networkData.getValue();
    Entity entity;
    Coordinates coordinates = null;
    UUID uuid = null;
    Health health = null;

    if (classString.equals(DirtBlock.class.getName())) {
      entity = blockFactory.createDirt(new Coordinates(0, 0));
    } else if (classString.equals(SkyBlock.class.getName())) {
      entity = blockFactory.createSky(new Coordinates(0, 0));
    } else if (classString.equals(StoneBlock.class.getName())) {
      entity = blockFactory.createStone(new Coordinates(0, 0));
    } else if (classString.equals(Ladder.class.getName())) {
      entity = entityFactory.createLadder(new Coordinates(0, 0));
    } else if (classString.equals(Turret.class.getName())) {
      entity = entityFactory.createTurret(new Coordinates(0, 0));
    } else if (classString.equals(Projectile.class.getName())) {
      entity = entityFactory.createProjectile(new Coordinates(0, 0));
    } else if (classString.equals(Orb.class.getName())) {
      entity = entityFactory.createOrb(new Coordinates(0, 0));
    } else if (classString.equals(Entity.class.getName())) {
      entity = entityFactory.createEntity(new Coordinates(0, 0));
    } else {
      throw new SerializationDataMissing("classString not recognized");
    }

    List<Attribute> attributeList = new LinkedList<>();
    for (NetworkObjects.NetworkData networkDataChild : networkData.getChildrenList()) {
      if (networkDataChild.getKey().equals(DataTranslationEnum.COORDINATES)) {
        coordinates = createCoordinates(networkDataChild);
      } else if (networkDataChild.getKey().equals(UUID.class.getName())) {
        uuid = UUID.fromString(networkDataChild.getValue());
      } else if (networkDataChild.getKey().equals(DataTranslationEnum.HEALTH)) {
        health = createHealth(networkDataChild);
      } else if (Arrays.asList(DataTranslationEnum.ITEM_TYPES)
          .contains(networkDataChild.getKey())) {
        attributeList.add(createAttribute(networkDataChild));
      }
    }

    if (uuid == null) throw new SerializationDataMissing("Missing UUID");
    if (coordinates == null) throw new SerializationDataMissing("Missing coordinates");
    if (health == null) throw new SerializationDataMissing("Missing health");
    entity.setUuid(uuid);
    entity.coordinates = coordinates;
    entity.health = health;

    for (Attribute attr : attributeList) {
      entity.updateAttribute(attr);
    }

    return entity;
  }

  public ReplaceBlockIncomingEventType createReplaceBlockIncomingEventType(
      NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
    UserID user = null;
    UUID target = null;
    Entity replacementBlock = null;
    ChunkRange chunkRange = null;

    if (!networkEvent.getUser().isEmpty()) {
      user = UserID.createUserID(networkEvent.getUser());
    }

    for (NetworkObjects.NetworkData child : networkEvent.getData().getChildrenList()) {
      switch (child.getKey()) {
        case DataTranslationEnum.UUID:
          target = createUUID(child);
          break;
        case DataTranslationEnum.CHUNK_RANGE:
          chunkRange = createChunkRange(child);
          break;
        default:
          replacementBlock = this.createEntity(child);
          break;
      }
    }
    if (target == null) throw new SerializationDataMissing("Missing target uuid");
    if (chunkRange == null) throw new SerializationDataMissing("Missing chunkRange");
    if (replacementBlock == null) throw new SerializationDataMissing("Missing replacementBlock");

    return EventTypeFactory.createReplaceBlockIncomingEvent(
        user, target, (Block) replacementBlock, chunkRange);
  }
}
