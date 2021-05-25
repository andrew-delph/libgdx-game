package infra.common.events;

import infra.serialization.SerializationEvent;

import java.util.HashMap;

public abstract class Event implements SerializationEvent {

  public abstract String getType();

  HashMap<String, Object> data;

  public Event() {
    this.data = new HashMap<>();
  }

  public Event(HashMap<String, Object> data) {
    this.data = data;
  }

  HashMap<String, Object> getData() {
    return this.data;
  }
}
