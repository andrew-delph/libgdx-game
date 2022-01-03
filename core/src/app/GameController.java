package app;

import chunk.ChunkRange;
import com.badlogic.gdx.math.Vector2;
import com.google.inject.Inject;
import common.Coordinates;
import common.Direction;
import common.GameStore;
import common.events.EventService;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.EntityFactory;
import entity.block.*;
import entity.misc.Ladder;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;

import java.util.UUID;

public class GameController {

    @Inject
    GameStore gameStore;

    @Inject
    EntityFactory entityFactory;

    @Inject
    EventService eventService;

    @Inject
    EventTypeFactory eventTypeFactory;

    @Inject
    BlockFactory blockFactory;

    public Entity addEntity(Entity entity) {
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                EventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(entity.coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Entity triggerAddEntity(Entity entity) {
        this.gameStore.addEntity(entity);
        return entity;
    }

    public void removeEntity(UUID uuid) {
        Entity entity;
        try {
            entity = this.gameStore.getEntity(uuid);
        } catch (EntityNotFound e) {
            e.printStackTrace();
            return;
        }
        eventService.queuePostUpdateEvent(eventTypeFactory.createRemoveEntityEvent(uuid));
        eventService.fireEvent(EventTypeFactory.createRemoveEntityOutgoingEvent(
                entity.uuid, new ChunkRange(entity.coordinates)));
    }

    public Entity triggerRemoveEntity(UUID uuid) throws EntityNotFound {
        return this.gameStore.removeEntity(uuid);
    }

    public Entity createEntity(Coordinates coordinates) {
        Entity entity = entityFactory.createEntity();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                EventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Block createSkyBlock(Coordinates coordinates) {
        Block entity = blockFactory.createSky();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                EventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Block createDirtBlock(Coordinates coordinates) {
        Block entity = blockFactory.createDirt();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                EventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Block createStoneBlock(Coordinates coordinates) {
        Block entity = blockFactory.createStone();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                EventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Entity createLadder(Coordinates coordinates) throws EntityNotFound {
        if (!(this.gameStore.getBlock(coordinates) instanceof EmptyBlock)) {
            throw new EntityNotFound("Did not find EmptyBlock");
        }
        if (this.gameStore.getLadder(coordinates) != null) return this.gameStore.getLadder(coordinates);
        Entity entity = entityFactory.createLadder();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                EventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public void moveEntity(UUID uuid, Coordinates coordinates) throws EntityNotFound {
        Entity entity = this.gameStore.getEntity(uuid);
        entity.coordinates = coordinates;
        this.eventService.fireEvent(
                EventTypeFactory.createUpdateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates)));
    }

    public void placeBlock(Entity entity, Direction direction, Class blockClass) throws EntityNotFound {
        Block removeBlock = null;
        if (direction == Direction.LEFT) {
            removeBlock = this.gameStore.getBlock(entity.getCenter().getLeft());
        } else if (direction == Direction.RIGHT) {
            removeBlock = this.gameStore.getBlock(entity.getCenter().getRight());
        } else if (direction == Direction.UP) {
            removeBlock = this.gameStore.getBlock(entity.getCenter().getUp());
        } else if (direction == Direction.DOWN) {
            removeBlock = this.gameStore.getBlock(entity.getCenter().getDown());
        }
        if (removeBlock == null) throw new EntityNotFound("Block to remove not found in direction.");
        if (removeBlock.getClass() == blockClass) return;

        Block replacementBlock;
        if (blockClass == SkyBlock.class) {
            replacementBlock = blockFactory.createSky();
        } else if (blockClass == DirtBlock.class) {
            replacementBlock = blockFactory.createDirt();
        } else {
            return;
        }
        this.replaceBlock(removeBlock, replacementBlock);
    }

    public void replaceBlock(Block target, Block replacementBlock) {
        Ladder removeLadder = this.gameStore.getLadder(target.coordinates);
        if (removeLadder != null) {
            this.removeEntity(removeLadder.uuid);
        }
        // put this into a post update event
        this.eventService.queuePostUpdateEvent(
                EventTypeFactory.createReplaceEntityEvent(target.uuid, replacementBlock, new ChunkRange(target.coordinates)));
        this.eventService.fireEvent(
                EventTypeFactory.createReplaceBlockOutgoingEvent(
                        target.uuid, replacementBlock, new ChunkRange(target.coordinates)));
    }

    public Entity triggerReplaceEntity(UUID target, Entity replacementEntity) throws EntityNotFound {
        return triggerReplaceEntity(target, replacementEntity, false);
    }

    public Entity triggerReplaceEntity(UUID target, Entity replacementEntity, Boolean swapVelocity) throws EntityNotFound {
        Vector2 velocity = null;
        Entity removeEntity = this.gameStore.getEntity(target);
        if (swapVelocity) {
            velocity = removeEntity.getBody().getLinearVelocity();
        }
        replacementEntity.coordinates = removeEntity.coordinates;
        this.gameStore.removeEntity(removeEntity.uuid);

        this.gameStore.addEntity(replacementEntity);
        this.gameStore.removeEntity(replacementEntity.uuid);
        this.gameStore.addEntity(replacementEntity);

        if (swapVelocity) {
            replacementEntity.getBody().setLinearVelocity(velocity);
        }
        return replacementEntity;
    }

    public void syncEntity(Entity entity) throws EntityNotFound {
        if (!gameStore.getEntityChunkRange(entity.uuid).equals(new ChunkRange(entity.coordinates))) {
            this.eventService.queuePostUpdateEvent(
                    EventTypeFactory.createReplaceEntityEvent(entity.uuid, entity, new ChunkRange(entity.coordinates)));
        }
    }

    public void createAI() {
        this.eventService.queuePostUpdateEvent(
                this.eventTypeFactory.createAIEntityEventType(new Coordinates(0, 0)));
    }
}
