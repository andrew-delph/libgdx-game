package app.update;

import app.screen.BaseCamera;
import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import generation.ChunkGenerationManager;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StandAloneUpdateTask extends UpdateTask {

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
    BaseCamera baseCamera;

    public StandAloneUpdateTask() {
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        /*
        - create requested chunks from screen. send them to the generation manager
        - generate around active entities
        - don't delete chunks
         */

        this.clock.tick();
        List<Callable<Chunk>> callableChunkList =
                this.gameStore.getChunkOnClock(this.clock.currentTick);

        List<ChunkRange> chunkRangeOnScreen = baseCamera.getChunkRangeOnScreen();
        // send chunkRangeOnScreen to the generation

        callableChunkList.addAll(this.chunkGenerationManager.generateActiveEntities());

        try {
            executor.invokeAll(callableChunkList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.eventService.firePostUpdateEvents();
    }

}
