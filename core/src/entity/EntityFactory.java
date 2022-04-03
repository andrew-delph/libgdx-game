package entity;

import app.screen.BaseAssetManager;
import com.google.inject.Inject;
import common.Clock;
import common.Coordinates;
import entity.misc.Ladder;

public class EntityFactory {

  @Inject Clock clock;
  @Inject BaseAssetManager baseAssetManager;
  @Inject EntityBodyBuilder entityBodyBuilder;

  @Inject
  EntityFactory() {}

  public Entity createEntity(Coordinates coordinates) {
    Entity entity = new Entity(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return entity;
  }

  public Ladder createLadder(Coordinates coordinates) {
    Ladder ladder = new Ladder(clock, baseAssetManager, entityBodyBuilder, coordinates);
    return ladder;
  }
}
