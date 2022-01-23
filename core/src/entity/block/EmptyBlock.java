package entity.block;

import app.screen.BaseAssetManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import entity.EntityBodyBuilder;

public abstract class EmptyBlock extends Block {
    public EmptyBlock(
            Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
        super(clock, baseAssetManager, entityBodyBuilder);
    }

    @Override
    public Body addWorld(World world) {
        return EntityBodyBuilder.createEmptyBlockBody();
    }
}
