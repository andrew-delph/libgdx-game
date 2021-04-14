package generation.layer;

import infra.map.chunk.Chunk;

public abstract class AbstractLayer {
    int seed;

    public AbstractLayer() {
    }

    abstract public void generateLayer(Chunk chunk);

}
