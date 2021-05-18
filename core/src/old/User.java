package old.game;

import java.util.UUID;

public class User {
  UUID id = UUID.randomUUID();

  public UUID getId() {
    return this.id;
  }
}
