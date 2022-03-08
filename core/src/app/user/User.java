package app.user;

import com.google.inject.Inject;

public class User {
  private final UserID userID = UserID.createUserID();

  @Inject
  public User() {}

  public UserID getUserID() {
    return userID;
  }

  @Override
  public String toString() {
    return userID.toString();
  }
}
