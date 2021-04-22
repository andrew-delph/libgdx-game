package generation.noise;

import org.junit.Test;

public class TestFastNoiseGenerator {
    @Test
    public void printGetValue() {
        FastNoiseGenerator generator = new FastNoiseGenerator();
        generator.setBottomRange(0);
        generator.setTopRange(2);
        generator.setyScale(0.5f);
        for (int i = 0; i < 1000; i++) {
            System.out.println(generator.getValue(0, i));
        }
    }
}
