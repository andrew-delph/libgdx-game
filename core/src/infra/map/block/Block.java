package infra.map.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import infra.common.Coordinate;

public class Block {
    public Coordinate coordinate;
    Texture image;
    public Sprite sprite;

    public final int size;

    public Block(Coordinate coordinate, int size) {
        this.coordinate = coordinate;
        this.size = size;
        this.sprite = new Sprite();
        this.sprite.setSize(this.size,this.size);
        this.sprite.setPosition(this.size*this.coordinate.getX(),this.size*this.coordinate.getY());
    }
}
