package render;

import base.BaseApplicationAdapter;
import base.BaseAssetManager;
import base.BaseCamera;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import generation.noise.FastNoiseLite;
import infra.entity.Entity;
import infra.entity.factories.EntityFactory;
import infra.map.WorldMap;
import infra.map.block.Block;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MapRender extends BaseApplicationAdapter {

    public int size = 1;
    public int width = size * 1280;
    public int height = size * 720;
    SpriteBatch batch;
    float[] noiseData;

    Entity entity;
    Sprite sprite;

    @Inject
    EntityFactory entityFactory;

    @Inject
    RenderManager renderManager;

    @Inject
    WorldMap worldMap;

    @Inject
    public MapRender(BaseAssetManager assetManager, BaseCamera camera) {
        super(assetManager, camera);
    }


    @Override
    public void create() {
        this.init();
        this.batch = new SpriteBatch();

        this.entity = entityFactory.createBasic();
        this.sprite = new Sprite(assetManager.get("frog.png", Texture.class));
//        this.sprite.setTexture(assetManager.get("frog.png", Texture.class));
//        this.sprite.setPosition();
        this.sprite.setSize(200,100);

        noiseData = new float[width * height];
        int index = 0;
        FastNoiseLite noise = new FastNoiseLite(ThreadLocalRandom.current().nextInt());
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noiseData[index++] = ((noise.GetNoise(x, y) + 1) * 5) / 10.0f;
            }
        }
        worldMap.generateArea(0,0,20,20);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



//        ShapeRenderer shapeRenderer = new ShapeRenderer();
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        for (int i = 0; i < noiseData.length; i++) {
//            shapeRenderer.setColor(0.5f, noiseData[i], 0.5f, 1);
//            shapeRenderer.rect((i % width) * size, ((i / width)) * size, size, size);
//        }
//        shapeRenderer.end();
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

//        renderManager.render(this.entity, batch);

        this.sprite.draw(batch);

        List<Block> blocks = worldMap.getBlocksInRange(0,0,20,20);

        for (Block block : blocks) {
            renderManager.render(block, batch);
        }

        batch.end();

        handleInput();

        System.out.println(camera.position.x+", "+camera.position.y+", "+camera.position.z);
        System.out.println(Arrays.toString(camera.projection.getValues()));

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 1);
        }

        camera.update();
    }


}
