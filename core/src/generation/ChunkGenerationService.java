package generation;

import chunk.ChunkRange;
import com.google.inject.Inject;
import configuration.GameSettings;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunkGenerationService {

    private final ExecutorService executor = Executors.newFixedThreadPool(GameSettings.GENERATION_THREADS);
    private final Set<ChunkRange> generatedSet = new HashSet<>();

    @Inject
    ChunkBuilderFactory chunkBuilderFactory;

    public synchronized void queueChunkRangeToGenerate(Set<ChunkRange> toGenerateSet) {
        for (ChunkRange toGenerate : toGenerateSet) {
            if (generatedSet.contains(toGenerate)) continue;
            generatedSet.add(toGenerate);
            executor.submit(chunkBuilderFactory.create(toGenerate));
        }
    }
}
