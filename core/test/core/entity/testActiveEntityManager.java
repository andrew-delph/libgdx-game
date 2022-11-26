package core.entity;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.user.UserID;
import core.configuration.BaseServerConfig;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class testActiveEntityManager {

  ActiveEntityManager activeEntityManager;
  Injector serverInjector;

  @Before
  public void setup() {
    serverInjector = Guice.createInjector(new BaseServerConfig());
    activeEntityManager = serverInjector.getInstance(ActiveEntityManager.class);
  }

  @Test
  public void testActiveEntityManager() {
    UserID testUserID = UserID.createUserID();
    UUID testUUID = UUID.randomUUID();

    activeEntityManager.registerActiveEntity(testUserID, testUUID);

    assert activeEntityManager.getUserActiveEntitySet(testUserID).contains(testUUID);
    assert activeEntityManager.getActiveEntities().contains(testUUID);

    activeEntityManager.deregisterUser(testUserID);

    assert !activeEntityManager.getActiveEntities().contains(testUUID);
  }
}
