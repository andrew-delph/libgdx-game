package infra.entity.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import infra.entity.Entity;

public class UserController extends Controller{
    public UserController(Entity entity) {
        super(entity);
    }

    @Override
    public void update() {
    System.out.println("user controller");
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.entity.coordinates = this.entity.coordinates.getLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.entity.coordinates = this.entity.coordinates.getRight();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.entity.coordinates = this.entity.coordinates.getDown();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.entity.coordinates = this.entity.coordinates.getUp();
        }
    System.out.println(entity.coordinates);
    }
}
