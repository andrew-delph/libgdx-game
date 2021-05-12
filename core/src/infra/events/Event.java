package infra.events;

import java.util.HashMap;

public interface Event {
  String getType();

  HashMap<String, Object> getData();
}
