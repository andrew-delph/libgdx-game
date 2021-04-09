package render;

import base.BaseAssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.Inject;
import infra.entity.Entity;
import infra.map.block.Block;
import infra.map.block.DirtBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RenderManager{

    @Inject
    BaseAssetManager assetManager;

    public RenderManager() {
        this.put(Entity.class, (e, batch)->{
            e.sprite.draw(batch);
        });

        this.put(Block.class, (b, batch)->{
            b.sprite.draw(batch);
        });
        this.put(DirtBlock.class, (b, batch)->{
            b.sprite.draw(batch);
        });

    }

    private Map<Class<? extends Object>, BiConsumer<?, Batch>> renderConsumerMap = new HashMap<>();

    public <T> BiConsumer<T, Batch> put(Class<T> key, BiConsumer<T, Batch> c) {
        return (BiConsumer<T, Batch>) renderConsumerMap.put(key, c);
    }

    public <T> BiConsumer<T, Batch> get(Class<T> key) {
        return (BiConsumer<T, Batch>) renderConsumerMap.get(key);
    }

    public <T> void render(T o, Batch batch){
        BiConsumer<T, Batch> renderConsumer = (BiConsumer<T, Batch>) this.get(o.getClass());
        renderConsumer.accept(o, batch);
    }
}
