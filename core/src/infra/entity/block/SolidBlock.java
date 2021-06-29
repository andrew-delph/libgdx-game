package infra.entity.block;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class SolidBlock extends Block{
    @Override
    public Body addWorld(World world) {
        return this.entityBodyBuilder.createSolidBlockBody(world, this.coordinates);
    }
}
