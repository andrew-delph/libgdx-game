package render;

import base.BaseAssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.Inject;
import infra.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RenderManager{

    @Inject
    BaseAssetManager assetManager;

    public RenderManager() {
        this.put(Entity.class, (e, batch)->{
            System.out.println("andrew"+assetManager);
            System.out.println("rupert"+assetManager.getAssetNames().size);
            Texture texture = assetManager.get("badlogic.jpg", Texture.class);
            batch.draw(texture,e.getX(),e.getY());
            System.out.println("Drawinng enntity: "+e.getID().toString());
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
