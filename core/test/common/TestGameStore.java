package common;

import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.exceptions.EntityNotFound;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestGameStore {

    Injector injector;
    GameStore gameStore;

    EntityFactory entityFactory;

    ChunkFactory chunkFactory;

    @Before
    public void setup() throws IOException {
        injector = Guice.createInjector(new ClientConfig());
        gameStore = injector.getInstance(GameStore.class);
        entityFactory = injector.getInstance(EntityFactory.class);
        chunkFactory = injector.getInstance(ChunkFactory.class);
    }

    @Test
    public void testEntityExistence() throws EntityNotFound {
        Entity testEntity = entityFactory.createEntity();
        this.gameStore.addChunk(this.chunkFactory.create(new ChunkRange(new Coordinates(0, 0))));
        gameStore.addEntity(testEntity);
        assert testEntity == gameStore.getEntity(testEntity.uuid);
    }

    @Test
    public void testChunkExistence() {
        Chunk chunk = this.chunkFactory.create(new ChunkRange(new Coordinates(0, 0)));
        this.gameStore.addChunk(chunk);
        assert chunk == gameStore.getChunk(new ChunkRange(new Coordinates(0, 0)));
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

        this.gameStore.syncEntity(entity);

        assert this.gameStore.getEntityChunk(entity.uuid).chunkRange == chunkRange2;
    }
}
