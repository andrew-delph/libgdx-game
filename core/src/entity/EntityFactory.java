package entity;

import app.screen.BaseAssetManager;
import com.google.inject.Inject;
import common.Clock;
import entity.misc.Ladder;

public class EntityFactory {

    @Inject
    Clock clock;
    @Inject
    BaseAssetManager baseAssetManager;
    @Inject
    EntityBodyBuilder entityBodyBuilder;

    @Inject
    EntityFactory() {
    }

    public Entity createEntity() {
        return new Entity(clock, baseAssetManager, entityBodyBuilder);
    }

    public Ladder createLadder() {
        return new Ladder(clock, baseAssetManager, entityBodyBuilder);
    }
}
