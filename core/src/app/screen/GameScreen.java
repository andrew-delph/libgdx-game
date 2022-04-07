package app.screen;

import app.GameController;
import app.game.Game;
import app.user.User;
import chunk.Chunk;
import chunk.ChunkRange;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.google.inject.Inject;
import common.Coordinates;
import common.GameSettings;
import common.GameStore;
import common.exceptions.ChunkNotFound;
import common.exceptions.SerializationDataMissing;
import common.exceptions.WrongVersion;
import entity.ActiveEntityManager;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.EntityControllerFactory;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameScreen extends ApplicationAdapter {

  public static ShapeRenderer pathDebugRender;
  final Logger LOGGER = LogManager.getLogger();
  @Inject Game game;
  @Inject GameStore gameStore;
  @Inject EntityFactory entityFactory;
  @Inject BaseAssetManager baseAssetManager;
  @Inject BaseCamera baseCamera;
  @Inject GameController gameController;
  @Inject EntityControllerFactory entityControllerFactory;
  @Inject ActiveEntityManager activeEntityManager;
  @Inject User user;
  Box2DDebugRenderer debugRenderer;
  Matrix4 debugMatrix;
  Entity myEntity;
  SpriteBatch batch;

  @Inject
  public GameScreen() {}

  @Override
  public void create() {
    baseAssetManager.init();
    baseCamera.init();
    try {
      game.start();
    } catch (IOException | InterruptedException | SerializationDataMissing | WrongVersion e) {
      LOGGER.error(e, e);
      this.dispose();
    }
    batch = new SpriteBatch();
    batch.enableBlending();

    myEntity = entityFactory.createEntity(new Coordinates(0, 2));
    try {
      myEntity = gameController.addEntity(myEntity);
    } catch (ChunkNotFound e) {
      LOGGER.error(e);
      this.dispose();
    }
    LOGGER.info("my entity " + myEntity.uuid);
    myEntity.setController(entityControllerFactory.createEntityUserController(myEntity));
    activeEntityManager.registerActiveEntity(user.getUserID(), myEntity.getUuid());
    debugRenderer = new Box2DDebugRenderer();
    pathDebugRender = new ShapeRenderer();
    pathDebugRender.setColor(Color.RED);
  }

  @Override
  public void resize(int width, int height) {
    LOGGER.info("resize:" + width + "," + height);
    baseCamera.setToOrtho(false, width, height);
  }

  @Override
  public void render() {
    debugMatrix =
        batch
            .getProjectionMatrix()
            .cpy()
            .scale(
                ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                0);
    baseCamera.position.set(
        myEntity.coordinates.getXReal() * GameSettings.PIXEL_SCALE,
        myEntity.coordinates.getYReal() * GameSettings.PIXEL_SCALE,
        0);
    baseCamera.update();
    // focus camera
    batch.setProjectionMatrix(baseCamera.combined);
    // clear screen
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();

    if (GameSettings.RENDER_DEBUG) {
      pathDebugRender.begin(ShapeRenderer.ShapeType.Line);
      pathDebugRender.setProjectionMatrix(debugMatrix);
    }

    List<Entity> renderList =
        gameStore.getEntityInRange(
            baseCamera.getBottomLeftCoordinates(), baseCamera.getTopRightCoordinates());

    renderList =
        renderList.stream()
            .sorted(Comparator.comparingInt(entity -> entity.zindex))
            .collect(Collectors.toList());

    for (Entity entity : renderList) {
      // render entity
      try {
        entity.renderSync();
        entity.sprite.draw(batch);
        if (entity.entityController != null) entity.entityController.render();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    batch.end();

    if (GameSettings.RENDER_DEBUG) {

      Chunk mainChunk = this.gameStore.getChunk((new ChunkRange(myEntity.coordinates)));
      debugRenderer.render(mainChunk.world, debugMatrix);
      pathDebugRender.end();

      debugMatrix =
          batch
              .getProjectionMatrix()
              .cpy()
              .scale(
                  ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                  ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                  0)
              .translate(0, 100, 0);
      Chunk lowerChunk = this.gameStore.getChunk((new ChunkRange(myEntity.coordinates)).getDown());
      Chunk leftChunk = this.gameStore.getChunk((new ChunkRange(myEntity.coordinates)).getLeft());
      Chunk rightChunk = this.gameStore.getChunk((new ChunkRange(myEntity.coordinates)).getRight());

      if (lowerChunk == null) return;
      debugMatrix =
          batch
              .getProjectionMatrix()
              .cpy()
              .scale(
                  ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                  ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                  0)
              .translate(
                  0,
                  -10
                      - GameSettings.PIXEL_SCALE
                          * GameSettings.CHUNK_SIZE
                          * ((float) GameSettings.PHYSICS_SCALE / GameSettings.PIXEL_SCALE),
                  0);
      debugRenderer.render(lowerChunk.world, debugMatrix);

      if (leftChunk == null) return;
      debugMatrix =
          batch
              .getProjectionMatrix()
              .cpy()
              .scale(
                  ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                  ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                  0)
              .translate(
                  -10
                      - GameSettings.PIXEL_SCALE
                          * GameSettings.CHUNK_SIZE
                          * ((float) GameSettings.PHYSICS_SCALE / GameSettings.PIXEL_SCALE),
                  0,
                  0);
      debugRenderer.render(leftChunk.world, debugMatrix);

      if (rightChunk == null) return;
      debugMatrix =
          batch
              .getProjectionMatrix()
              .cpy()
              .scale(
                  ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                  ((float) GameSettings.PIXEL_SCALE / GameSettings.PHYSICS_SCALE),
                  0)
              .translate(
                  10
                      + GameSettings.PIXEL_SCALE
                          * GameSettings.CHUNK_SIZE
                          * ((float) GameSettings.PHYSICS_SCALE / GameSettings.PIXEL_SCALE),
                  0,
                  0);
      debugRenderer.render(rightChunk.world, debugMatrix);
    }
  }

  @Override
  public void dispose() {
    this.game.stop();
  }
}
