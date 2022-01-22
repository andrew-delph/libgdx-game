package app.update;

import app.screen.GameScreen;
import chunk.Chunk;
import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import generation.ChunkGenerationManager;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientUpdateTask extends UpdateTask {
    @Inject
    public Clock clock;
    @Inject
    public GameStore gameStore;
    public ExecutorService executor;
    @Inject
    EventService eventService;
    @Inject
    ChunkGenerationManager chunkGenerationManager;
    @Inject
    GameScreen gameScreen;

    public ClientUpdateTask() {
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {

        /*
        -create requested chunks and send request to subscription manager
        -delete chunks no longer needs
         */
        this.clock.tick();
        List<Callable<Chunk>> callableChunkList =
                this.gameStore.getChunkOnClock(this.clock.currentTick);

        callableChunkList.addAll(this.chunkGenerationManager.generateActiveEntities());

        try {
            executor.invokeAll(callableChunkList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.eventService.firePostUpdateEvents();
    }
}
