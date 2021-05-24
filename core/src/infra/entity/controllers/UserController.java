package infra.entity.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import infra.common.networkobject.Coordinates;
import infra.entity.Entity;

public class UserController extends Controller{
    public UserController(Entity entity) {
        super(entity);
    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.entity.coordinates = new Coordinates(this.entity.coordinates.getXReal()-0.1f,this.entity.coordinates.getYReal());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.entity.coordinates = new Coordinates(this.entity.coordinates.getXReal()+0.1f,this.entity.coordinates.getYReal());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.entity.coordinates = new Coordinates(this.entity.coordinates.getXReal(),this.entity.coordinates.getYReal()-0.1f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.entity.coordinates = new Coordinates(this.entity.coordinates.getXReal(),this.entity.coordinates.getYReal()+0.1f);
        }
    }
}
