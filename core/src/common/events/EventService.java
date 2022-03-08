package common.events;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.*;
import java.util.function.Consumer;

public class EventService {

  Map<String, List<Consumer<common.events.types.EventType>>> eventListeners = new HashMap<>();
  Map<String, List<Consumer<common.events.types.EventType>>> eventPostUpdateListeners =
      new HashMap<>();
  List<common.events.types.EventType> postUpdateEventTypeList = new LinkedList<>();

  @Inject
  public EventService() {}

  public void addListener(String type, Consumer<common.events.types.EventType> eventConsumer) {
    this.eventListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventListeners.get(type).add(eventConsumer);
  }

  public void fireEvent(common.events.types.EventType eventType) {
    if (this.eventListeners.get(eventType.getType()) != null) {
      this.eventListeners
          .get(eventType.getType())
          .forEach(eventConsumer -> eventConsumer.accept(eventType));
    }
  }

  public void addPostUpdateListener(
      String type, Consumer<common.events.types.EventType> eventConsumer) {
    this.eventPostUpdateListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventPostUpdateListeners.get(type).add(eventConsumer);
  }

  public void queuePostUpdateEvent(common.events.types.EventType eventType) {
    this.postUpdateEventTypeList.add(eventType);
  }

  public void firePostUpdateEvents() {
    List<common.events.types.EventType> postUpdateEventTypeListCopy =
        new LinkedList<>(this.postUpdateEventTypeList);
    this.postUpdateEventTypeList = new LinkedList<>();
    for (EventType eventType : postUpdateEventTypeListCopy) {
      if (this.eventPostUpdateListeners.get(eventType.getType()) != null) {
        this.eventPostUpdateListeners
            .get(eventType.getType())
            .forEach(eventConsumer -> eventConsumer.accept(eventType));
      }
    }
  }
}
