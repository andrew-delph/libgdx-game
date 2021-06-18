package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.SoloConfig;
import infra.common.Coordinates;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import org.junit.Test;

public class testVertex {

    @Test
    public void testVertex(){
        Injector injector = Guice.createInjector(new SoloConfig());

        EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

        Entity entity = entityFactory.createEntity();
        Coordinates coordinates = new Coordinates(0,0);
        Vector2 vector2 = new Vector2(0,0);

        assert new Vertex(entity,coordinates,vector2).equals(new Vertex(entity,new Coordinates(0,0),new Vector2(0,0)));
    }
}
