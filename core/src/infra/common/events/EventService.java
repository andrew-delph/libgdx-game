package infra.common.events;

import com.google.inject.Inject;

import java.util.*;
import java.util.function.Consumer;

public class EventService {

  Map<String, List<Consumer<Event>>> eventListeners = new HashMap<>();
  Map<String, List<Consumer<Event>>> eventPostUpdateListeners = new HashMap<>();
  List<Event> postUpdateEventList = new LinkedList<>();

  @Inject
  public EventService() {}

  public void addListener(String type, Consumer<Event> eventConsumer) {
    this.eventListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventListeners.get(type).add(eventConsumer);
  }

  public void fireEvent(Event event) {
    if (this.eventListeners.get(event.getType()) != null) {
      this.eventListeners
          .get(event.getType())
          .forEach(eventConsumer -> eventConsumer.accept(event));
    }
  }

  public void addPostUpdateListener(String type, Consumer<Event> eventConsumer) {
    this.eventPostUpdateListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventPostUpdateListeners.get(type).add(eventConsumer);
  }

  public void queuePostUpdateEvent(Event event) {
    this.postUpdateEventList.add(event);
  }

  public void firePostUpdateEvents() {
    List<Event> postUpdateEventListCopy = new LinkedList<>(this.postUpdateEventList);
    this.postUpdateEventList = new LinkedList<>();
    for (Event event : postUpdateEventListCopy) {
      if (this.eventPostUpdateListeners.get(event.getType()) != null) {
        this.eventPostUpdateListeners
                .get(event.getType())
                .forEach(eventConsumer -> eventConsumer.accept(event));
      }
    }
  }
}
