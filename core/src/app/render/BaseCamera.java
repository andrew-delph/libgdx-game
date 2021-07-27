package app.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.inject.Inject;
import chunk.ChunkRange;
import common.Coordinates;
import entity.Entity;

import java.util.List;

public class BaseCamera extends OrthographicCamera {
  @Inject
  public BaseCamera() {}

  @Override
  public void update() {
    super.update();
  }

  public void init() {
    this.setToOrtho(false, 500, 500);
    this.update();
  }

  public List<ChunkRange> getChunkRangeOnScreen() {

    int left_x = (int) ((this.position.x - (this.viewportWidth / 2)) / Entity.coordinatesScale);
    int right_x = (int) ((this.position.x + (this.viewportWidth / 2)) / Entity.coordinatesScale);
    int bottom_y = (int) ((this.position.y - (this.viewportHeight / 2)) / Entity.coordinatesScale);
    int top_y = (int) ((this.position.y + (this.viewportHeight / 2)) / Entity.coordinatesScale);

    Coordinates bottomLeftCoordinates = new Coordinates(left_x, bottom_y);

    Coordinates topRightCoordinates = new Coordinates(right_x, top_y);

    ChunkRange bottomLeftChunkRange = new ChunkRange(bottomLeftCoordinates).getLeft().getDown();

    ChunkRange topRightChunkRange = new ChunkRange(topRightCoordinates).getRight().getUp();

    return ChunkRange.getChunkRangeListTwoPoints(
        new Coordinates(bottomLeftChunkRange.bottom_x, bottomLeftChunkRange.bottom_y),
        new Coordinates(topRightChunkRange.top_x, topRightChunkRange.top_y));
  }
}
