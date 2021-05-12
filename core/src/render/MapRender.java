package render;

import base.BaseApplicationAdapter;
import base.BaseAssetManager;
import base.BaseCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import generation.MapBuilder;
import infra.common.Coordinate;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.map.WorldMap;
import infra.map.block.Block;
import infra.map.chunk.Chunk;

import java.util.List;
import java.util.UUID;

public class MapRender extends BaseApplicationAdapter {

    public int size = 1;
    public int width = size * 1280;
    public int height = size * 720;
    SpriteBatch batch;
    float[] noiseData;

    Entity entity;
    Sprite sprite;
    Chunk chunk;

    @Inject
    EntityFactory entityFactory;

    @Inject
    RenderManager renderManager;

    @Inject
    WorldMap worldMap;

    @Inject
    MapBuilder mapBuilder;

    @Inject
    public MapRender(BaseAssetManager assetManager, BaseCamera camera) {
        super(assetManager, camera);
    }

    @Override
    public void create() {
        this.init();
        this.batch = new SpriteBatch();

        this.entity = entityFactory.create(UUID.randomUUID(), 50, 50, UUID.randomUUID());
        //        this.sprite = new Sprite(assetManager.get("frog.png", Texture.class));
        //        this.sprite.setTexture(assetManager.get("frog.png", Texture.class));
        //        this.sprite.setPosition();
        //        this.sprite.setSize(200,100);

        //        noiseData = new float[width * height];
        //        int index = 0;
        //        FastNoiseLite noise = new FastNoiseLite(ThreadLocalRandom.current().nextInt());
        //        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        //        for (int y = 0; y < height; y++) {
        //            for (int x = 0; x < width; x++) {
        //                noiseData[index++] = ((noise.GetNoise(x, y) + 1) * 5) / 10.0f;
        //            }
        //        }

        chunk = worldMap.mapGrid.getChunk(new Coordinate(0, 0));
        mapBuilder.generateWorld(chunk);
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

        handleInput();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldMap.cameraGenerateArea(camera);
        List<Block> blocks = worldMap.cameraGetBlocks(camera);
        for (Block block : blocks) {
            renderManager.render(block, batch);
        }
        renderManager.render(this.entity, batch);
        batch.end();

        //        System.out.println(camera.position.x+", "+camera.position.y+", "+camera.position.z);
        //        System.out.println(Arrays.toString(camera.projection.getValues()));
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void handleInput() {
        int moveDistance = 5;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.entity.moveX(-moveDistance);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.entity.moveX(moveDistance);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.entity.moveY(-moveDistance);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.entity.moveY(moveDistance);
        }
        camera.position.set(this.entity.getX(), this.entity.getY(), 0);
        camera.update();
    }
}
