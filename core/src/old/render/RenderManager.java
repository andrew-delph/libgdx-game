package old.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.Inject;
import old.base.BaseAssetManager;
import old.infra.entity.Entity;
import old.infra.map.block.AirBlock;
import old.infra.map.block.Block;
import old.infra.map.block.DirtBlock;
import old.infra.map.block.StoneBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class RenderManager {

  private final Map<Class<? extends Object>, BiConsumer<?, Batch>> renderConsumerMap =
      new HashMap<>();
  @Inject BaseAssetManager assetManager;

  public RenderManager() {
    this.put(
        Entity.class,
        (e, batch) -> {
          e.sprite.draw(batch);
        });

    this.put(
        Block.class,
        (b, batch) -> {
          b.sprite.draw(batch);
        });
    this.put(
        DirtBlock.class,
        (b, batch) -> {
          b.sprite.draw(batch);
        });
    this.put(
        StoneBlock.class,
        (b, batch) -> {
          b.sprite.draw(batch);
        });
    this.put(
        AirBlock.class,
        (b, batch) -> {
          b.sprite.draw(batch);
        });
  }

  public <T> BiConsumer<T, Batch> put(Class<T> key, BiConsumer<T, Batch> c) {
    return (BiConsumer<T, Batch>) renderConsumerMap.put(key, c);
  }

  public <T> BiConsumer<T, Batch> get(Class<T> key) {
    return (BiConsumer<T, Batch>) renderConsumerMap.get(key);
  }

  public <T> void render(T o, Batch batch) {
    BiConsumer<T, Batch> renderConsumer = (BiConsumer<T, Batch>) this.get(o.getClass());
    if (renderConsumer == null) {
      throw new NullPointerException("No old.render consumer found!");
    }
    renderConsumer.accept(o, batch);
  }
}
