package old.infra.factories;

import com.google.inject.Guice;
import com.google.inject.Injector;
import old.configure.ClientTestApp;
import old.infra.entity.Entity;
import old.infra.entity.EntityFactory;
import org.junit.Test;

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
    Injector injector = Guice.createInjector(new ClientTestApp());
    Entity test = injector.getInstance(EntityFactory.class).createBasic();
    UUID.fromString(test.toEntityData().getID());
    assertEquals(0.0, test.getX(), .1);
    assertEquals(0.0, test.getY(), .1);
  }
}
