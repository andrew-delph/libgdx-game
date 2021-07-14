package infra.entity.misc;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import infra.common.Clock;
import infra.app.render.BaseAssetManager;
import infra.entity.Entity;
import infra.entity.EntityBodyBuilder;

public class Ladder extends Entity {
  public Ladder(
      Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
    this.zindex = 2;
    this.textureName = "ladder.png";
    this.setWidth(Entity.coordinatesScale);
    this.setHeight(Entity.coordinatesScale);
  }

  @Override
  public synchronized Body addWorld(World world) {
    return this.entityBodyBuilder.createEmptyLadderBody(world, this.coordinates);
  }
}
