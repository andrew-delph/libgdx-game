package app;

import chunk.ChunkRange;
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

    public Entity createEntity(Entity entity) {
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                eventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(entity.coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Entity createSkyBlock(Coordinates coordinates) {
        Entity entity = blockFactory.createSky();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                eventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Entity createDirtBlock(Coordinates coordinates) {
        Entity entity = blockFactory.createDirt();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                eventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Entity createStoneBlock(Coordinates coordinates) {
        Entity entity = blockFactory.createStone();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                eventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Entity triggerCreateEntity(Entity entity) {
        this.gameStore.addEntity(entity);
        return entity;
    }

    public void moveEntity(UUID uuid, Coordinates coordinates) throws EntityNotFound {
        Entity entity = this.gameStore.getEntity(uuid);
        entity.coordinates = coordinates;
        this.eventService.fireEvent(
                eventTypeFactory.createUpdateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates)));
    }

    public void triggerMoveEntity(Entity entity, Coordinates coordinates) {
        entity.coordinates = coordinates;
        this.eventService.fireEvent(
                eventTypeFactory.createUpdateEntityOutgoingEvent(
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

        Ladder removeLadder = this.gameStore.getLadder(removeBlock.coordinates);
        if (removeLadder != null) {
            this.gameStore.removeEntity(removeLadder.uuid);
        }
        // put this into a post update event
        this.eventService.queuePostUpdateEvent(
                this.eventTypeFactory.createReplaceBlockEvent(removeBlock.uuid, replacementBlock));
        this.eventService.fireEvent(
                this.eventTypeFactory.createReplaceBlockOutgoingEvent(
                        removeBlock.uuid, replacementBlock, new ChunkRange(removeBlock.coordinates)));
    }

    public Entity createLadder(Coordinates coordinates) throws EntityNotFound {
        if (this.gameStore.getBlock(coordinates) instanceof SolidBlock) {
            throw new EntityNotFound("Did not find SolidBlock");
        }
        if (this.gameStore.getLadder(coordinates) != null) return this.gameStore.getLadder(coordinates);
        Entity entity = entityFactory.createLadder();
        entity.coordinates = coordinates;
        this.gameStore.addEntity(entity);
        CreateEntityOutgoingEventType createEntityOutgoingEvent =
                eventTypeFactory.createCreateEntityOutgoingEvent(
                        entity.toNetworkData(), new ChunkRange(coordinates));
        this.eventService.fireEvent(createEntityOutgoingEvent);
        return entity;
    }

    public Entity replaceBlock(UUID target, Block replacementBlock) throws EntityNotFound {
        Entity removeBlock = this.gameStore.removeEntity(target);
        if (removeBlock == null) throw new EntityNotFound("Could not find block to remove.");
        replacementBlock.coordinates = removeBlock.coordinates;
        this.gameStore.addEntity(replacementBlock);
        return replacementBlock;
    }

    public void createAI() {
        this.eventService.queuePostUpdateEvent(
                this.eventTypeFactory.createAIEntityEventType(new Coordinates(0, 0)));
    }
}
