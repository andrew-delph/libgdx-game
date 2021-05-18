package old.infra.map.block;

import com.badlogic.gdx.graphics.Texture;
import old.infra.common.Coordinate;

public class AirBlock extends Block {
  public AirBlock(Coordinate coordinate, int size, Texture texture) {
    super(coordinate, size, texture);
  }

  public AirBlock(Coordinate coordinate, int size) {
    super(coordinate, size);
  }
}
