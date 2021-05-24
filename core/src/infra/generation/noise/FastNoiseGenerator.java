package infra.generation.noise;

import java.util.concurrent.ThreadLocalRandom;

public class FastNoiseGenerator{

    FastNoiseLite noise;
    float xScale = 1;
    float yScale = 1;
    int bottomRange = 0;
    int topRange = 1;

    public FastNoiseGenerator() {
        noise = new FastNoiseLite(ThreadLocalRandom.current().nextInt());
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
    }

    public void setxScale(float xScale) {
        this.xScale = xScale;
    }

    public void setyScale(float yScale) {
        this.yScale = yScale;
    }

    public void setBottomRange(int bottomRange) {
        this.bottomRange = bottomRange;
    }

    public void setTopRange(int topRange) {
        this.topRange = topRange;
    }

    public void setNoiseType(FastNoiseLite.NoiseType type) {
        noise.SetNoiseType(type);
    }

    public int getValue(int x, int y) {
        return (int) this.getFloatValue(x, y);
    }

    public float getFloatValue(int x, int y) {
        return bottomRange
                + (((noise.GetNoise(x * this.xScale, y * this.yScale) + 1) / 2) * (topRange - bottomRange));
    }
}
