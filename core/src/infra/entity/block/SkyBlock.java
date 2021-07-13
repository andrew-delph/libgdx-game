package infra.entity.block;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import infra.common.Clock;
import infra.common.render.BaseAssetManager;
import infra.entity.EntityBodyBuilder;

public class SkyBlock extends EmptyBlock {
  public SkyBlock(Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
    super(clock, baseAssetManager, entityBodyBuilder);
    this.textureName = "sky.png";
  }
}
