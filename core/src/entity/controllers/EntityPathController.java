package entity.controllers;

import app.GameController;
import common.Coordinates;
import common.events.EventService;
import entity.Entity;
import entity.EntityFactory;
import entity.controllers.actions.EntityActionFactory;
import entity.pathfinding.PathGuider;
import entity.pathfinding.PathGuiderFactory;
import networking.events.EventTypeFactory;

public class EntityPathController extends EntityController {

    PathGuiderFactory pathGuiderFactory;

    EventService eventService;
    EventTypeFactory eventTypeFactory;
    EntityFactory entityFactory;

    PathGuider pathGuider;
    Entity target;
    Coordinates beforeUpdateCoordinates = null;

    EntityPathController(
            GameController gameController,
            EntityActionFactory entityActionFactory,
            PathGuiderFactory pathGuiderFactory,
            EventService eventService,
            EventTypeFactory eventTypeFactory,
            EntityFactory entityFactory,
            Entity entity,
            Entity target) {
        super(gameController, entityActionFactory, eventService, eventTypeFactory, entity);
        this.pathGuiderFactory = pathGuiderFactory;
        this.eventService = eventService;
        this.target = target;
        this.eventTypeFactory = eventTypeFactory;
        this.entityFactory = entityFactory;
    }

    @Override
    public void afterWorldUpdate() {
        super.afterWorldUpdate();
    }


    @Override
    public void render() {
        if (this.pathGuider != null) this.pathGuider.render();
    }

    @Override
    public void beforeWorldUpdate() {
        this.beforeUpdateCoordinates = this.entity.coordinates;
        if (this.pathGuider == null) {
            this.pathGuider = pathGuiderFactory.createPathGuider(entity);
        }
        if (this.entity.coordinates.getBase().equals(target.coordinates.getBase())) {
            eventService.queuePostUpdateEvent(eventTypeFactory.createRemoveEntityEvent(entity.uuid));
            return;
        }
        try {
            this.pathGuider.followPath(target.coordinates);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
