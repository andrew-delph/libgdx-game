package infra.entity;

import com.google.inject.Inject;
import infra.app.render.BaseAssetManager;
import infra.common.Clock;
import infra.entity.misc.Ladder;

public class EntityFactory {

  @Inject Clock clock;
  @Inject BaseAssetManager baseAssetManager;
  @Inject EntityBodyBuilder entityBodyBuilder;

  @Inject
  EntityFactory() {}

  public Entity createEntity() {
    return new Entity(clock, baseAssetManager, entityBodyBuilder);
  }

  public Ladder createLadder() {
    return new Ladder(clock, baseAssetManager, entityBodyBuilder);
  }
}
