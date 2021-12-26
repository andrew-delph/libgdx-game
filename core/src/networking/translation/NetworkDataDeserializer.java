package networking.translation;

import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameStore;
import common.exceptions.EntityNotFound;
import common.exceptions.SerializationDataMissing;
import entity.Entity;
import entity.EntityFactory;
import entity.block.*;
import entity.misc.Ladder;
import networking.NetworkObjects;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.incoming.HandshakeIncomingEventType;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.incoming.UpdateEntityIncomingEventType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class NetworkDataDeserializer {
    @Inject
    EntityFactory entityFactory;
    @Inject
    BlockFactory blockFactory;
    @Inject
    ChunkFactory chunkFactory;
    @Inject
    GameStore gameStore;
    @Inject
    EventTypeFactory eventTypeFactory;

    public Chunk createChunk(NetworkObjects.NetworkData networkData) throws SerializationDataMissing {
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
        Chunk chunkToCreate = chunkFactory.create(chunkRange);
        chunkToCreate.chunkRange = chunkRange;
        chunkToCreate.addAllEntity(entityList);
        return chunkToCreate;
    }

    public Entity createEntity(NetworkObjects.NetworkData networkData) throws SerializationDataMissing {
        String classString = networkData.getValue();
        Entity entity;
        Coordinates coordinates = null;
        UUID uuid = null;

        if (classString.equals(DirtBlock.class.getName())) {
            entity = blockFactory.createDirt();
        } else if (classString.equals(SkyBlock.class.getName())) {
            entity = blockFactory.createSky();
        } else if (classString.equals(StoneBlock.class.getName())) {
            entity = blockFactory.createStone();
        } else if (classString.equals(Ladder.class.getName())) {
            entity = entityFactory.createLadder();
        } else if (classString.equals(Entity.class.getName())) {
            entity = entityFactory.createEntity();
        } else {
            throw new SerializationDataMissing("classString not recognized");
        }
        for (NetworkObjects.NetworkData networkDataChild : networkData.getChildrenList()) {
            if (networkDataChild.getKey().equals(Coordinates.class.getName())) {
                coordinates = this.createCoordinates(networkDataChild);
            } else if (networkDataChild.getKey().equals(UUID.class.getName())) {
                uuid = UUID.fromString(networkDataChild.getValue());
            }
        }

        if (uuid == null) throw new SerializationDataMissing("Missing UUID");
        if (coordinates == null) throw new SerializationDataMissing("Missing coordinates");
        entity.uuid = uuid;
        entity.coordinates = coordinates;
        return entity;
    }

    public Coordinates createCoordinates(NetworkObjects.NetworkData networkData) {
        //TODO put in translations
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

    public static ChunkRange createChunkRange(NetworkObjects.NetworkData networkData) {
        //TODO put in translations
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

    public Entity updateEntity(NetworkObjects.NetworkData networkData) throws EntityNotFound, SerializationDataMissing {
        Coordinates coordinates = null;
        UUID uuid = null;
        for (NetworkObjects.NetworkData networkDataChild : networkData.getChildrenList()) {
            if (networkDataChild.getKey().equals(Coordinates.class.getName())) {
                coordinates = this.createCoordinates(networkDataChild);
            } else if (networkDataChild.getKey().equals(UUID.class.getName())) {
                uuid = UUID.fromString(networkDataChild.getValue());
            }
        }

        if (uuid == null) throw new SerializationDataMissing("Missing UUID");
        if (coordinates == null) throw new SerializationDataMissing("Missing coordinates");
        Entity entity = this.gameStore.getEntity(uuid);
        entity.coordinates = coordinates;
        return entity;
    }

    public static UUID createUUID(NetworkObjects.NetworkData networkData) {
        return UUID.fromString(networkData.getValue());
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

    public static HandshakeIncomingEventType createHandshakeIncomingEventType(NetworkObjects.NetworkEvent networkEvent) {
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
        UUID connectionUUID = null;
        if (!networkEvent.getUser().isEmpty()) {
            connectionUUID = UUID.fromString(networkEvent.getUser());
        }
        return EventTypeFactory.createHandshakeIncomingEventType(connectionUUID, chunkRange, uuidList);
    }

    public ReplaceBlockIncomingEventType createReplaceBlockIncomingEventType(NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
        UUID user = null;
        UUID target = null;
        Entity replacementBlock = null;
        ChunkRange chunkRange = null;

        if (!networkEvent.getUser().isEmpty()) {
            user = UUID.fromString(networkEvent.getUser());
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

        return EventTypeFactory.createReplaceBlockIncomingEvent(user, target, (Block) replacementBlock, chunkRange);
    }

    public static UpdateEntityIncomingEventType createUpdateEntityIncomingEvent(NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
        UUID user = null;
        ChunkRange chunkRange = null;
        NetworkObjects.NetworkData networkData = null;

        if (!networkEvent.getUser().isEmpty()) {
            user = UUID.fromString(networkEvent.getUser());
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

        return EventTypeFactory.createUpdateEntityIncomingEvent(user, networkData, chunkRange);
    }

    public static CreateEntityIncomingEventType createCreateEntityIncomingEventType(NetworkObjects.NetworkEvent networkEvent) throws SerializationDataMissing {
        UUID user = null;
        ChunkRange chunkRange = null;
        NetworkObjects.NetworkData networkData = null;

        if (!networkEvent.getUser().isEmpty()) {
            user = UUID.fromString(networkEvent.getUser());
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

        return EventTypeFactory.createCreateEntityIncomingEvent(user, networkData, chunkRange);
    }

    
}
