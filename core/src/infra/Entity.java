package infra;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.UUID;

public class Entity {

    Texture img;
    int x, y;
    UUID id;

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.img = new Texture("badlogic.jpg");
        this.id = UUID.randomUUID();
    }

    void update(SpriteBatch batch){
        batch.draw(img, this.x, this.y);
    }

    public void moveX(int move){
        this.x += move;
    }

    public void moveY(int move){
        this.y += move;
    }
}
