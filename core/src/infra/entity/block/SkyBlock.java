package infra.entity.block;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import infra.common.Clock;
import infra.common.render.BaseAssetManager;

public class SkyBlock extends Block {
  public SkyBlock(Clock clock, BaseAssetManager baseAssetManager) {
    super(clock, baseAssetManager);
    this.textureName = "sky.png";
  }

  @Override
  public Body addWorld(World world) {
    return null;
  }
}
