package infra.map.block;

import com.badlogic.gdx.graphics.Texture;
import infra.common.Coordinate;
import infra.entity.Entity;

import java.util.UUID;

public class Block extends Entity {
  public Coordinate coordinate;

  public Block(UUID id, Coordinate coordinate, UUID owner, int size, Texture texture) {
    this(id, coordinate, owner, size);
    this.sprite.setTexture(texture);
  }

  public Block(UUID id, Coordinate coordinate, UUID owner, int size) {
    super(id, coordinate.getX(), coordinate.getY(), owner);
    this.coordinate = coordinate;
    this.size = size;
    this.sprite.setSize(this.size, this.size);
    this.sprite.setPosition(this.size * coordinate.getX(), this.size * coordinate.getY());
  }
}
