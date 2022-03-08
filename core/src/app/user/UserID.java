package app.user;

import java.util.UUID;

public class UserID {

  private final UUID id;

  private UserID(UUID id) {
    this.id = id;
  }

  public static UserID createUserID(String id) {
    return new UserID(UUID.fromString(id));
  }

  public static UserID createUserID() {
    return new UserID(UUID.randomUUID());
  }

  @Override
  public String toString() {
    return id.toString();
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    UserID other = (UserID) obj;
    return this.id.equals(other.id);
  }
}
