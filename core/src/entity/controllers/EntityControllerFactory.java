package entity.controllers;

import app.GameController;
import com.google.inject.Inject;
import common.events.EventService;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.actions.EntityActionFactory;
import entity.pathfinding.PathGuiderFactory;
import networking.events.EventTypeFactory;

public class EntityControllerFactory {
    @Inject
    GameController gameController;
    @Inject
    EntityActionFactory entityActionFactory;
    @Inject
    PathGuiderFactory pathGuiderFactory;
    @Inject
    EventService eventService;
    @Inject
    EntityFactory entityFactory;
    @Inject
    EventTypeFactory eventTypeFactory;

    @Inject
    public EntityControllerFactory() {
    }

    public EntityUserController createEntityUserController(Entity entity) {
        return new EntityUserController(
                gameController, entityActionFactory, eventService, eventTypeFactory, entity);
    }

    public EntityPathController createEntityPathController(Entity source, Entity target) {
        return new EntityPathController(
                gameController,
                entityActionFactory,
                pathGuiderFactory,
                eventService,
                eventTypeFactory,
                entityFactory,
                source,
                target);
    }
}
