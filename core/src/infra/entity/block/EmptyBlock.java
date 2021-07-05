package infra.entity.block;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class EmptyBlock extends Block {
  @Override
  public Body addWorld(World world) {
    return entityBodyBuilder.createEmptyBlockBody();
  }
}
