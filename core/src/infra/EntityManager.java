package infra;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    private List<Entity> entityList;


    public EntityManager() {
        entityList = new ArrayList<>();
    }


    public void add(Entity entity){
        this.entityList.add(entity);
    }

    public void updateAll(SpriteBatch batch){
        for (Entity element : this.entityList){
            element.update(batch);
        }
    }

}
