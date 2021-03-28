package infra.map.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import infra.common.Coordinate;

public class Block {
    public Coordinate coordinate;
    Texture image;
    Sprite sprite;

    public final int size;

    public Block(Coordinate coordinate, int size) {
        this.coordinate = coordinate;
        this.size = size;
//        this.image = new Texture("badlogic.jpg");
//        this.sprite = new Sprite(this.image);
//        this.sprite.setOrigin(0, 0);
//        this.sprite.setPosition(this.coordinate.getX(), this.coordinate.getY());
//        this.sprite.setSize(size, size);
    }
}
