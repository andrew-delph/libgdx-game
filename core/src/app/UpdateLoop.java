package app;

import chunk.Chunk;
import com.google.inject.Inject;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import generation.ChunkGenerationManager;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class UpdateLoop extends TimerTask {
}
