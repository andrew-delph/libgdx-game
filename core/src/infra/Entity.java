package infra;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity {

    Texture img;
    int x, y;

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.img = new Texture("badlogic.jpg");
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
