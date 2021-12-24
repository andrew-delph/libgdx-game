package networking.translation;

import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameStore;
import entity.Entity;
import entity.EntityFactory;
import entity.block.BlockFactory;
import entity.block.DirtBlock;
import entity.block.SkyBlock;
import entity.block.StoneBlock;
import entity.misc.Ladder;
import networking.NetworkObjects;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.HandshakeIncomingEventType;

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

    public Chunk createChunk(NetworkObjects.NetworkData networkData) {
        List<Entity> entityList = new LinkedList<>();
        ChunkRange chunkRange = null;
        for (NetworkObjects.NetworkData networkDataChild : networkData.getChildrenList()) {
            String classString = networkDataChild.getValue();

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

    public Entity createEntity(NetworkObjects.NetworkData networkData) {
        String classString = networkData.getValue();
        Entity entity;
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
            return null;
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

    public static ChunkRange createChunkRange(NetworkObjects.NetworkData networkData) {
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

    public static HandshakeIncomingEventType createHandshakeIncomingEventType(NetworkObjects.NetworkData networkData) {
        ChunkRange chunkRange = null;
        List<UUID> uuidList = null;
        for (NetworkObjects.NetworkData child : networkData.getChildrenList()) {
            switch (child.getKey()) {
                case DataTranslationEnum.UUID_LIST:
                    uuidList = createUUIDList(child);
                    break;
                case DataTranslationEnum.CHUNK_RANGE:
                    chunkRange = createChunkRange(child);
                    break;
            }
        }
        return EventTypeFactory.createHandshakeIncomingEventType(chunkRange, uuidList);
    }
}
