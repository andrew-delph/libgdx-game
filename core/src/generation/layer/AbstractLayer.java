package generation.layer;

import infra.map.chunk.Chunk;

public abstract class AbstractLayer {
    int seed;

    public AbstractLayer() {
    }

    public abstract void generateLayer(Chunk chunk);
}
