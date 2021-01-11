package infra.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.UUID;

public class Entity {

    EntityData data;

    public Entity(UUID id, int x, int y) {
        this.data = new EntityData();
        this.data.setId(id);
        this.data.setX(x);
        this.data.setY(y);
        this.data.setImg(new Texture("badlogic.jpg"));
    }

    public Entity(EntityData data) {
        this.data = data;
        this.data.setImg(new Texture("badlogic.jpg"));
    }

    public EntityData getEntityData() {
        return this.data;
    }

    public void updateEntityData(EntityData data) {
        this.data.merge(data);
    }

    void update(SpriteBatch batch) {
        batch.draw(this.data.getImg(), this.data.getX(), this.data.getY());
    }

    public void moveX(int move) {
        this.data.setX(this.data.getX() + move);
    }

    public void moveY(int move) {
        this.data.setY(this.data.getY() + move);
    }
}
