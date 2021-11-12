package common.events;

import com.google.inject.Inject;

import java.util.*;
import java.util.function.Consumer;

public class EventService {

  Map<String, List<Consumer<EventType>>> eventListeners = new HashMap<>();
  Map<String, List<Consumer<EventType>>> eventPostUpdateListeners = new HashMap<>();
  List<EventType> postUpdateEventTypeList = new LinkedList<>();

  @Inject
  public EventService() {}

  public void addListener(String type, Consumer<EventType> eventConsumer) {
    this.eventListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventListeners.get(type).add(eventConsumer);
  }

  public void fireEvent(EventType eventType) {
    if (this.eventListeners.get(eventType.getType()) != null) {
      this.eventListeners
          .get(eventType.getType())
          .forEach(eventConsumer -> eventConsumer.accept(eventType));
    }
  }

  public void addPostUpdateListener(String type, Consumer<EventType> eventConsumer) {
    this.eventPostUpdateListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventPostUpdateListeners.get(type).add(eventConsumer);
  }

  public void queuePostUpdateEvent(EventType eventType) {
    this.postUpdateEventTypeList.add(eventType);
  }

  public void firePostUpdateEvents() {
    List<EventType> postUpdateEventTypeListCopy = new LinkedList<>(this.postUpdateEventTypeList);
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
