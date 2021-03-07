package infra.factories;


import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import infra.entity.Entity;
import infra.entity.factories.EntityFactory;
import modules.App;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestEntityFactory {


//    @Test
//    public void createFromEntityData() {
//        HashMap<String, String> map = new HashMap<>();
//        String x = "0";
//        String y = "0";
//        String id = UUID.randomUUID().toString();
//        map.put("x", x);
//        map.put("y", y);
//        map.put("id", id);
//        assertEquals(1, 1);
//    }

    @Test
    public void createBasic() {
        Injector injector = Guice.createInjector(new App());
        Entity test = injector.getInstance(EntityFactory.class).createBasic();
        UUID.fromString(test.getEntityData().getID());
        assertEquals("0", test.getEntityData().getX());
        assertEquals("0", test.getEntityData().getY());
    }

}
