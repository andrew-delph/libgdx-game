package app.client;

import app.UpdateLoop;
import app.render.BaseCamera;
import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import com.google.inject.Inject;
import common.events.EventService;
import common.exceptions.SerializationDataMissing;
import generation.ChunkGenerationManager;
import networking.client.ClientNetworkHandle;
import networking.events.EventTypeFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class ClientUpdateLoop extends UpdateLoop {

    @Inject
    ChunkGenerationManager chunkGenerationManager;

    @Inject
    ChunkFactory chunkFactory;

    @Inject
    ClientNetworkHandle clientNetworkHandle;

    @Inject
    EventTypeFactory eventTypeFactory;

    @Inject
    BaseCamera baseCamera;

    @Inject
    EventService eventService;

    @Override
    public void run() {
        this.clock.tick();
        List<Callable<Chunk>> callableChunkList =
                this.gameStore.getChunkOnClock(this.clock.currentTick);

        List<ChunkRange> chunkRangeListOnCamera = baseCamera.getChunkRangeOnScreen();

        Set<ChunkRange> subscribeChunkRange = new HashSet<>();
        for (ChunkRange chunkRange : chunkRangeListOnCamera) {
            if (this.gameStore.getChunk(chunkRange) == null) {
                try {
                    this.gameStore.addChunk(this.clientNetworkHandle.getChunk(chunkRange));
                } catch (SerializationDataMissing e) {
                    e.printStackTrace();
                }
            }
            subscribeChunkRange.add(chunkRange);
        }

        if (subscribeChunkRange.size() > 0) {
            this.clientNetworkHandle.send(
                    eventTypeFactory
                            .createSubscriptionOutgoingEvent(new LinkedList<>(subscribeChunkRange))
                            .toNetworkEvent());
        }

        try {
            executor.invokeAll(callableChunkList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.eventService.firePostUpdateEvents();
    }
}
