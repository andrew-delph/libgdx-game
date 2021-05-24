package infra.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import infra.common.GameStore;
import infra.common.render.BaseAssetManager;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.controllers.UserController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreen extends ApplicationAdapter {

  @Inject Game game;

  @Inject
  GameStore gameStore;

  @Inject
  EntityFactory entityFactory;

  SpriteBatch batch;

  @Inject
  BaseAssetManager baseAssetManager;

  @Override
  public void create() {
    baseAssetManager.init();
    baseAssetManager.get("badlogic.jpg");
    game.start();
    batch = new SpriteBatch();
    Entity myEntity = entityFactory.createEntity();
    myEntity.setController(new UserController(myEntity));
    gameStore.addEntity(myEntity);
  }

  @Override
  public void render() {
    batch.begin();

    List<Entity> renderList = game.getEntityListInRange(0, 0, 100, 100);

    try{
      renderList = renderList.stream().sorted(Comparator.comparingInt(entity -> entity.zindex)).collect(Collectors.toList());
    }
    catch (Exception e){
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
