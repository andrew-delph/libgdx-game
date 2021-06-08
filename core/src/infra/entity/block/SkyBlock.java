package infra.entity.block;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class SkyBlock extends Block {
  public SkyBlock() {
    super();
    this.textureName = "sky.png";
  }

  @Override
  public Body addWorld(World world) {
    return null;
  }
}
