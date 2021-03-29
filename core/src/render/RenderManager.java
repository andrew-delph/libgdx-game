package render;

import base.BaseAssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.Inject;
import infra.entity.Entity;
import infra.map.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RenderManager{

    @Inject
    BaseAssetManager assetManager;

    public RenderManager() {
        this.put(Entity.class, (e, batch)->{
            Texture texture = assetManager.get("badlogic.jpg", Texture.class);
            batch.draw(texture,e.getX(),e.getY());
        });

        this.put(Block.class, (b, batch)->{
            // TODO dont assign texture here
            if(b.sprite.getTexture() == null){
                Texture texture = assetManager.get("badlogic.jpg", Texture.class);
                Pixmap pixmap = new Pixmap(256,128, Pixmap.Format.RGBA8888);
                double rand = Math.random();
                if (rand<0.3){
                    pixmap.setColor(Color.YELLOW);
                }
                else if(rand < 0.6){
                    pixmap.setColor(Color.BLUE);
                }
                else{
                    pixmap.setColor(Color.GREEN);
                }
                pixmap.fill();
                b.sprite.setTexture(new Texture(pixmap));
            }
            b.sprite.draw(batch);
//            batch.draw(b.sprite);
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
