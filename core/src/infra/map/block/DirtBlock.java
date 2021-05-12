package infra.map.block;

import com.badlogic.gdx.graphics.Texture;
import infra.common.Coordinate;

import java.util.UUID;

public class DirtBlock extends Block {

    public DirtBlock(UUID id, Coordinate coordinate, UUID owner, int size, Texture texture) {
        super(id, coordinate, owner, size, texture);
    }

    public DirtBlock(UUID id, Coordinate coordinate, UUID owner, int size) {
        super(id, coordinate, owner, size);
    }
}
