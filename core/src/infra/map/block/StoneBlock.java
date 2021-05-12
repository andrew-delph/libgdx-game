package infra.map.block;

import com.badlogic.gdx.graphics.Texture;
import infra.common.Coordinate;

public class StoneBlock extends Block {
    public StoneBlock(Coordinate coordinate, int size, Texture texture) {
        super(coordinate, size, texture);
    }

    public StoneBlock(Coordinate coordinate, int size) {
        super(coordinate, size);
    }
}
