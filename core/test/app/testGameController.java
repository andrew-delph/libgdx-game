package app;

import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.GameStore;
import common.events.EventService;
import common.exceptions.EntityNotFound;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class testGameController {

    Injector injector;
    GameStore gameStore;
    GameController gameController;
    EntityFactory entityFactory;
    ChunkFactory chunkFactory;
    EventService eventService;

    @Before
    public void setup() throws IOException {
        injector = Guice.createInjector(new ClientConfig());
        gameStore = injector.getInstance(GameStore.class);
        gameController = injector.getInstance(GameController.class);
        entityFactory = injector.getInstance(EntityFactory.class);
        chunkFactory = injector.getInstance(ChunkFactory.class);
        eventService = injector.getInstance(EventService.class);
    }

    @Test
    public void testEntitySync() throws EntityNotFound {
        ChunkRange chunkRange1 = new ChunkRange(new Coordinates(0, 0));
        ChunkRange chunkRange2 = chunkRange1.getRight();
        this.gameStore.addChunk(this.chunkFactory.create(chunkRange1));
        this.gameStore.addChunk(this.chunkFactory.create(chunkRange2));
        Entity entity = this.entityFactory.createEntity();
        this.gameStore.addEntity(entity);
        assert this.gameStore.getEntityChunk(entity.uuid).chunkRange == chunkRange1;
        entity.coordinates = new Coordinates(chunkRange2.bottom_x, chunkRange2.bottom_y);
        this.gameController.syncEntity(entity);
        eventService.firePostUpdateEvents();
        assert this.gameStore.getEntityChunk(entity.uuid).chunkRange == chunkRange2;
    }
}
