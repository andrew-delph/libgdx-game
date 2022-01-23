package app.update;

import app.screen.BaseCamera;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import entity.ActiveEntityManager;
import generation.ChunkGenerationManager;
import generation.ChunkGenerationService;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StandAloneUpdateTask extends UpdateTask {

    private final ExecutorService executor = Executors.newCachedThreadPool();


    @Inject
    public Clock clock;
    @Inject
    public GameStore gameStore;
    @Inject
    EventService eventService;
    @Inject
    ChunkGenerationManager chunkGenerationManager;
    @Inject
    BaseCamera baseCamera;
    @Inject
    ActiveEntityManager activeEntityManager;
    @Inject
    ChunkGenerationService chunkGenerationService;

    public StandAloneUpdateTask() {
    }

    @Override
    public void run() {
        /*
        - create requested chunks from screen. send them to the generation manager
        - generate around active entities
        - don't delete chunks
         */
        Set<ChunkRange> requiredChunkRanges = new HashSet<>();

        // get the set of onscreen chunks
        requiredChunkRanges.addAll(baseCamera.getChunkRangeOnScreen());
        // get the set of active entities. get their chunks
        requiredChunkRanges.addAll(activeEntityManager.getActiveChunkRanges());
        // generate them all
        chunkGenerationService.queueChunkRangeToGenerate(requiredChunkRanges);

        this.clock.tick();
        try {
            executor.invokeAll(this.gameStore.getChunkOnClock(this.clock.currentTick));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.eventService.firePostUpdateEvents();
    }

}
