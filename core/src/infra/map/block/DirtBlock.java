package infra.map.block;

import com.badlogic.gdx.graphics.Texture;
import infra.common.Coordinate;

public class DirtBlock extends Block {
  public DirtBlock(Coordinate coordinate, int size, Texture texture) {
    super(coordinate, size, texture);
  }

  public DirtBlock(Coordinate coordinate, int size) {
    super(coordinate, size);
  }
}
