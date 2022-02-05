package entity.misc;

import app.screen.BaseAssetManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import entity.Entity;
import entity.EntityBodyBuilder;

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
        return EntityBodyBuilder.createEmptyLadderBody(world, this.coordinates);
    }
}
