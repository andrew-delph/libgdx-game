package infra.map.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import infra.common.Coordinate;

public class Block {
    public final int size;
    public Coordinate coordinate;
    public Sprite sprite;

    public Block(Coordinate coordinate, int size, Texture texture) {
        this(coordinate, size);
        this.sprite.setTexture(texture);
    }

    public Block(Coordinate coordinate, int size) {
        this.coordinate = coordinate;
        this.size = size;
        this.sprite = new Sprite();
        this.sprite.setSize(this.size, this.size);
        this.sprite.setPosition(this.size * this.coordinate.getX(), this.size * this.coordinate.getY());
    }
}
