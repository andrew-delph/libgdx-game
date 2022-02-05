package app.screen;

import chunk.ChunkRange;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.inject.Inject;
import common.Coordinates;
import entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class BaseCamera extends OrthographicCamera {
    @Inject
    public BaseCamera() {
    }

    @Override
    public void update() {
        super.update();
    }

    public void init() {
        this.setToOrtho(false, 500, 500);
        this.update();
    }

    public Set<ChunkRange> getChunkRangeOnScreen() {
        int left_x = (int) ((this.position.x - (this.viewportWidth / 2)) / Entity.coordinatesScale);
        int bottom_y = (int) ((this.position.y - (this.viewportHeight / 2)) / Entity.coordinatesScale);
        int right_x = (int) ((this.position.x + (this.viewportWidth / 2)) / Entity.coordinatesScale);
        int top_y = (int) ((this.position.y + (this.viewportHeight / 2)) / Entity.coordinatesScale);

        Coordinates bottomLeftCoordinates = (new Coordinates(left_x, bottom_y)).getLeft().getDown();
        Coordinates topRightCoordinates = (new Coordinates(right_x, top_y)).getRight().getUp();

        return new HashSet<>(ChunkRange.getChunkRangeListTwoPoints(
                bottomLeftCoordinates,
                topRightCoordinates));
    }
}
