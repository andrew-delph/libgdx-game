package configuration;

import app.render.BaseAssetManager;
import chunk.ChunkFactory;
import chunk.ChunkSubscriptionManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import common.ChunkClockMap;
import common.Clock;
import common.GameStore;
import common.events.EventService;
import entity.EntityFactory;
import entity.block.BlockFactory;
import entity.collision.CollisionService;
import entity.collision.EntityContactListenerFactory;
import entity.collision.ground.EntityGroundContact;
import entity.collision.ladder.EntityLadderContact;
import entity.controllers.EntityControllerFactory;
import entity.controllers.actions.EntityActionFactory;
import entity.pathfinding.EdgeStore;
import generation.BlockGenerator;
import generation.ChunkBuilderFactory;
import generation.ChunkGenerationManager;
import networking.ConnectionStore;
import networking.ObserverFactory;
import networking.events.EventTypeFactory;

public abstract class MainConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(BaseAssetManager.class).in(Singleton.class);
        bind(Clock.class).asEagerSingleton();
        bind(GameStore.class).asEagerSingleton();
        bind(ChunkGenerationManager.class).asEagerSingleton();
        bind(BlockGenerator.class).asEagerSingleton();
        bind(EventService.class).asEagerSingleton();
        bind(ConnectionStore.class).asEagerSingleton();
        bind(ChunkSubscriptionManager.class).asEagerSingleton();

        bind(ChunkClockMap.class).asEagerSingleton();
        //
        bind(ChunkFactory.class).asEagerSingleton();
        //
        bind(EntityFactory.class).asEagerSingleton();
        bind(BlockFactory.class).asEagerSingleton();

        bind(ChunkBuilderFactory.class).asEagerSingleton();

        bind(EntityControllerFactory.class).asEagerSingleton();

        bind(EntityActionFactory.class).asEagerSingleton();
        bind(EntityContactListenerFactory.class).asEagerSingleton();
        bind(EntityGroundContact.class).asEagerSingleton();
        bind(EdgeStore.class).asEagerSingleton();

        //
        bind(EventTypeFactory.class).asEagerSingleton();
        //
        bind(ObserverFactory.class).asEagerSingleton();
        bind(EntityLadderContact.class).asEagerSingleton();
        bind(CollisionService.class).asEagerSingleton();
    }
}
