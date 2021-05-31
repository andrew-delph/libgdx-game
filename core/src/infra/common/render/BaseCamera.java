package infra.common.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.inject.Inject;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public class BaseCamera extends OrthographicCamera {
  @Override
  public void update() {
    super.update();
  }

  public void init() {
    this.setToOrtho(false, 500, 500);
    this.update();
  }

  @Inject
  public BaseCamera() {}

  public List<ChunkRange> getChunkRangeOnScreen() {
    List<ChunkRange> chunkRangeList = new LinkedList<>();

    int left_x = (int) ((this.position.x - (this.viewportWidth / 2)) / Entity.coordinatesScale);
    int top_y = (int) ((this.position.y + (this.viewportHeight / 2)) / Entity.coordinatesScale);

    ChunkRange top_left = new ChunkRange(new Coordinates(left_x, top_y));

    int x_ChunkRange_scale =
        (int) Math.ceil(viewportWidth / Entity.coordinatesScale / ChunkRange.size);
    int y_ChunkRange_scale =
        (int) Math.ceil(viewportHeight / Entity.coordinatesScale / ChunkRange.size);

    ChunkRange root = top_left.getUp();
    for (int i = 0; i <= y_ChunkRange_scale; i++) {
      root = root.getDown();
      chunkRangeList.add(root);
      for (int j = 1; j < x_ChunkRange_scale; j++) {
        chunkRangeList.add(chunkRangeList.get(chunkRangeList.size() - 1).getRight());
      }
    }

    return chunkRangeList;
  }
}
