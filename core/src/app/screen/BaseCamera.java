package app.screen;

import chunk.ChunkRange;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.inject.Inject;
import common.GameSettings;
import entity.attributes.Coordinates;
import java.util.HashSet;
import java.util.Set;

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

  public Set<ChunkRange> getChunkRangeOnScreen() {
    Coordinates bottomLeftCoordinates = this.getBottomLeftCoordinates();
    Coordinates topRightCoordinates = this.getTopRightCoordinates();

    int expandRange = 10;

    for (int i = 0; i < expandRange; i++) {
      bottomLeftCoordinates = bottomLeftCoordinates.getLeft().getDown();
      topRightCoordinates = topRightCoordinates.getRight().getUp();
    }

    return new HashSet<>(
        ChunkRange.getChunkRangeListTwoPoints(bottomLeftCoordinates, topRightCoordinates));
  }

  Coordinates getBottomLeftCoordinates() {
    int left_x = (int) (this.position.x - (this.viewportWidth / 2)) / GameSettings.PIXEL_SCALE;
    int bottom_y = (int) (this.position.y - (this.viewportHeight / 2)) / GameSettings.PIXEL_SCALE;
    return new Coordinates(left_x, bottom_y).getLeft().getDown();
  }

  Coordinates getTopRightCoordinates() {
    int right_x = (int) (this.position.x + (this.viewportWidth / 2)) / GameSettings.PIXEL_SCALE;
    int top_y = (int) (this.position.y + (this.viewportHeight / 2)) / GameSettings.PIXEL_SCALE;
    return (new Coordinates(right_x, top_y));
  }
}
