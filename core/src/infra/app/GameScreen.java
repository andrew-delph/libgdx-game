package infra.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import infra.chunk.ChunkFactory;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.common.render.BaseAssetManager;
import infra.common.render.BaseCamera;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.controllers.EntityControllerFactory;
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


  @Override
  public void create() {
    baseAssetManager.init();
    baseCamera.init();
    try {
      game.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
    batch = new SpriteBatch();
    myEntity = gameController.createEntity(entityFactory.createEntity());
    myEntity.setController(entityControllerFactory.createEntityUserController(myEntity));
    chunkGenerationManager.registerActiveEntity(myEntity);
  }

  @Override
  public void render() {

    baseCamera.position.set(
        myEntity.coordinates.getXReal() * myEntity.coordinatesScale,
        myEntity.coordinates.getYReal() * myEntity.coordinatesScale,
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
      entity.renderSync();
      entity.sprite.draw(batch);
    }
    batch.end();
  }

  @Override
  public void dispose() {
    this.game.stop();
  }
}
