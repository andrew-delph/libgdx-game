package infra.entity.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.app.GameController;
import infra.common.Coordinates;
import infra.common.Direction;
import infra.entity.Entity;
import infra.entity.block.DirtBlock;
import infra.entity.block.SkyBlock;
import infra.entity.controllers.actions.EntityActionFactory;
import infra.entity.pathfinding.template.PathGuider;
import infra.entity.pathfinding.template.PathGuiderFactory;

public class EntityPathController extends EntityController{

    @Inject
    GameController gameController;

    @Inject
    PathGuiderFactory pathGuiderFactory;
    PathGuider pathGuider;

    @Inject
    EntityPathController(EntityActionFactory entityActionFactory,@Assisted Entity entity) {
        super(entityActionFactory, entity);

    }

    @Override
    public void beforeWorldUpdate() {
        if(this.pathGuider == null){
            this.pathGuider = pathGuiderFactory.createPathGuider(entity);
        }
        if(!this.pathGuider.hasPath()){
            try {
                this.pathGuider.findPath(this.entity.coordinates, new Coordinates(6,1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.pathGuider.followPath();
    }

    @Override
    public void afterWorldUpdate() {
        gameController.moveEntity(
                this.entity.uuid,
                new Coordinates(
                        this.entity.getBody().getPosition().x / Entity.coordinatesScale,
                        this.entity.getBody().getPosition().y / Entity.coordinatesScale));
    }
}
