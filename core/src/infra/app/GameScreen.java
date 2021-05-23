package infra.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.GameStore;
import infra.common.networkobject.Coordinates;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.controllers.UserController;

public class GameScreen extends ApplicationAdapter {

  @Inject Game game;

  @Inject
  GameStore gameStore;

  @Inject
  ChunkFactory chunkFactory;

  @Inject
  EntityFactory entityFactory;

  SpriteBatch batch;

  @Override
  public void create() {
    game.start();
    batch = new SpriteBatch();
    this.gameStore.addChunk(this.chunkFactory.create(new ChunkRange(new Coordinates(0,0))));
    Entity myEntity = entityFactory.createEntity();
    myEntity.setController(new UserController(myEntity));

    gameStore.addEntity(myEntity);
  }

  @Override
  public void render() {
    batch.begin();
    for (Entity entity : game.getEntityListInRange(0, 0, 100, 100)) {
      // render entity
      entity.sync();
      entity.sprite.draw(batch);
    }
    batch.end();
  }

  @Override
  public void dispose() {
    this.game.stop();
  }

}
