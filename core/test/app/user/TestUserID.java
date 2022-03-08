package app.user;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class TestUserID {

  @Test
  public void testUserIDEqual() {
    UUID random = UUID.randomUUID();

    UserID uid1 = UserID.createUserID(random.toString());
    UserID uid2 = UserID.createUserID(random.toString());

    Assert.assertEquals(uid1, uid2);

    Assert.assertNotEquals(uid1, UserID.createUserID());
  }
}
