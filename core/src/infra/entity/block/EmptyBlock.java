package infra.entity.block;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import infra.app.render.BaseAssetManager;
import infra.common.Clock;
import infra.entity.EntityBodyBuilder;

public abstract class EmptyBlock extends Block {
  public EmptyBlock(
      Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
  }

  @Override
  public Body addWorld(World world) {
    return entityBodyBuilder.createEmptyBlockBody();
  }
}
