package infra.common.events;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// TODO change to Observer and Observable
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
}
