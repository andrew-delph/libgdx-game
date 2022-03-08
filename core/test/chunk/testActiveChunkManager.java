package chunk;

import app.user.UserID;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import configuration.BaseServerConfig;
import org.junit.Before;
import org.junit.Test;

public class testActiveChunkManager {
  ActiveChunkManager activeChunkManager;
  Injector serverInjector;

  @Before
  public void setup() {
    serverInjector = Guice.createInjector(new BaseServerConfig());
    activeChunkManager = serverInjector.getInstance(ActiveChunkManager.class);
  }

  @Test
  public void testActiveEntityManager() {
    UserID testUserID = UserID.createUserID();
    Coordinates testCoordinates = new Coordinates(0, 0);
    ChunkRange testChunkRange = new ChunkRange(testCoordinates);

    activeChunkManager.addUserChunkSubscriptions(testUserID, testChunkRange);

    assert activeChunkManager.getChunkRangeUsers(testChunkRange).contains(testUserID);
    assert activeChunkManager.getUserChunkRanges(testUserID).contains(testChunkRange);

    activeChunkManager.removeUser(testUserID);

    assert !activeChunkManager.getChunkRangeUsers(testChunkRange).contains(testUserID);
    assert !activeChunkManager.getUserChunkRanges(testUserID).contains(testChunkRange);
  }
}
