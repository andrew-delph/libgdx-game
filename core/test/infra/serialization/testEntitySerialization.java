package infra.serialization;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.MainConfig;
import infra.common.networkobject.Coordinates;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class testEntitySerialization {

  SerializationConverter serializationConverter;

  Injector injector;
  EntityFactory entityFactory;

  @Before
  public void setup() throws IOException {
    injector = Guice.createInjector(new MainConfig());
    entityFactory = injector.getInstance(EntityFactory.class);
    serializationConverter = injector.getInstance(SerializationConverter.class);
  }

  @Test
  public void testEntitySerialization() {
    Entity entityWrite = entityFactory.createEntity();
    entityWrite.coordinates = new Coordinates(2,3);
    Entity entityRead = serializationConverter.createEntity(entityWrite.toNetworkData());
    assert entityWrite.coordinates.equals(entityRead.coordinates);
    assert entityWrite.uuid.equals(entityRead.uuid);
  }
}
