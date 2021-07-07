package infra.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.google.inject.Inject;
import infra.chunk.Chunk;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.common.render.BaseAssetManager;
import infra.common.render.BaseCamera;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.controllers.EntityController;
import infra.entity.controllers.EntityControllerFactory;
import infra.entity.pathfinding.template.EdgeRegistration;
import infra.generation.ChunkGenerationManager;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreen extends ApplicationAdapter {

  @Inject Game game;

  @Inject GameStore gameStore;

  @Inject EntityFactory entityFactory;

  SpriteBatch batch;

  @Inject BaseAssetManager baseAssetManager;

  @Inject ChunkGenerationManager chunkGenerationManager;

  @Inject BaseCamera baseCamera;

  Entity myEntity;

  @Inject GameController gameController;

  @Inject ChunkFactory chunkFactory;

  @Inject EntityControllerFactory entityControllerFactory;

  @Inject
  EdgeRegistration edgeRegistration;

  Box2DDebugRenderer debugRenderer;
  Matrix4 debugMatrix;

  @Inject
  public GameScreen() {}

  @Override
  public void create() {
    baseAssetManager.init();
    baseCamera.init();
    try {
      game.start();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    batch = new SpriteBatch();

    //    try {
    //      TimeUnit.SECONDS.sleep(5);
    //    } catch (InterruptedException e) {
    //      e.printStackTrace();
    //    }

    myEntity = entityFactory.createEntity();
    myEntity.coordinates = new Coordinates(1, 0);
    myEntity = gameController.createEntity(myEntity);
    System.out.println("my entity " + myEntity.uuid);
    myEntity.setController(entityControllerFactory.createEntityUserController(myEntity));
    chunkGenerationManager.registerActiveEntity(myEntity, null);
    debugRenderer = new Box2DDebugRenderer();

    edgeRegistration.edgeRegistration();

    Entity pathEntity = entityFactory.createEntity();
    pathEntity.coordinates = new Coordinates(0,1);
    gameController.createEntity(pathEntity);
    pathEntity.setController(entityControllerFactory.createEntityPathController(pathEntity));
  }

  @Override
  public void render() {

    debugMatrix = batch.getProjectionMatrix().cpy().scale(1, 1, 0);

    baseCamera.position.set(
        myEntity.coordinates.getXReal() * Entity.coordinatesScale,
        myEntity.coordinates.getYReal() * Entity.coordinatesScale,
        0);
    baseCamera.update();

    // focus camera
    batch.setProjectionMatrix(baseCamera.combined);

    // clear screen
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.begin();

    List<Entity> renderList = game.getEntityListInRange(0, 0, 100, 100);

    try {
      renderList =
          renderList.stream()
              .sorted(Comparator.comparingInt(entity -> entity.zindex))
              .collect(Collectors.toList());
    } catch (Exception e) {
      System.out.println(e);
    }
    for (Entity entity : renderList) {
      // render entity
      try {
        entity.renderSync();
        entity.sprite.draw(batch);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    //    System.out.println(new ChunkRange(myEntity.coordinates)+";
    // "+myEntity.coordinates.getXReal()+"; "+myEntity.coordinates.getX());
    batch.end();
    Chunk mainChunk = this.gameStore.getChunk(new ChunkRange(new Coordinates(0, 0)));
    debugMatrix = batch.getProjectionMatrix().cpy().scale(0.5f, 0.5f, 0);
    debugRenderer.render(mainChunk.world, debugMatrix);
    //    try {
    //      mainChunk = this.gameStore.getChunk(new ChunkRange(new Coordinates(-1, 0)));
    //      debugMatrix = batch.getProjectionMatrix().cpy().scale(0.5f, 0.5f, 0);
    //      debugRenderer.render(mainChunk.world, debugMatrix);
    //
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    }
    //
    //    try {
    //      mainChunk = this.gameStore.getChunk(new ChunkRange(new Coordinates(0, -1)));
    //      debugMatrix = batch.getProjectionMatrix().cpy().scale(0.5f, 0.5f, 0);
    //      debugRenderer.render(mainChunk.world, debugMatrix);
    //
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    }
  }

  @Override
  public void dispose() {
    this.game.stop();
  }
}
