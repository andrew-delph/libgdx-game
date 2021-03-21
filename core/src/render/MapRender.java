package render;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.google.inject.Inject;
import generation.noise.FastNoiseLite;

import java.util.concurrent.ThreadLocalRandom;

public class MapRender extends ApplicationAdapter {

    public int size = 5;
    public int width = size*100;
    public int height = size*100;
    SpriteBatch batch;
    Texture image;
    float[] noiseData;


    @Inject
    public MapRender() {


        // Gather noise data


//        for (int i = 0; i < noiseData.length; i++) {
//            System.out.println(Arrays.toString(Arrays.copyOfRange(noiseData, i * 128, i * 128 + 128)));
//        }
    }

    @Override
    public void create() {
        this.image = new Texture("badlogic.jpg");
        this.batch = new SpriteBatch();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        noiseData = new float[width * height];
        int index = 0;
        FastNoiseLite noise = new FastNoiseLite(ThreadLocalRandom.current().nextInt());
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noiseData[index++] = ((noise.GetNoise(x, y) + 1) * 5) / 10.0f;
            }
        }

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < noiseData.length; i++) {
            shapeRenderer.setColor(0.5f, noiseData[i], 0.5f, 1);
            shapeRenderer.rect((i % width) * size, ((i / width)) * size, size, size);
        }
        shapeRenderer.end();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
    }


}
