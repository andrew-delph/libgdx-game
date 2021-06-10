package infra.common.events;

import com.google.inject.Inject;

import java.util.*;
import java.util.function.Consumer;

public class EventService {

  Map<String, List<Consumer<Event>>> eventListeners;

  @Inject
  public EventService() {
    this.eventListeners = new HashMap<>();
  }

  public void addListener(String type, Consumer<Event> eventConsumer) {
    if (!this.eventListeners.containsKey(type)) {
      this.eventListeners.put(type, new ArrayList<>());
    }
    this.eventListeners.get(type).add(eventConsumer);
  }

  public void fireEvent(Event event) {
    if (this.eventListeners.get(event.getType()) != null) {
      this.eventListeners
          .get(event.getType())
          .forEach(eventConsumer -> eventConsumer.accept(event));
    }
  }

  List<Event> postUpdateEventList = new LinkedList<>();

  public void addPostUpdateEvent(Event event) {
    System.out.println("addPostUpdateEvent");
    this.postUpdateEventList.add(event);
  }

  public void firePostUpdateEvents() {
    List<Event> postUpdateEventListCopy = new LinkedList<>(this.postUpdateEventList);
    this.postUpdateEventList = new LinkedList<>();
    for (Event event:postUpdateEventListCopy){
      this.eventListeners
              .get(event.getType())
              .forEach(eventConsumer -> eventConsumer.accept(event));
    }
  }
}
