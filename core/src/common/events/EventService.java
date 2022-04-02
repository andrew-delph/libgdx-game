package common.events;

import com.google.inject.Inject;
import common.events.types.EventType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class EventService {

  Map<String, List<Consumer<common.events.types.EventType>>> eventListeners = new HashMap<>();
  Map<String, List<Consumer<common.events.types.EventType>>> eventPostUpdateListeners =
      new HashMap<>();
  List<common.events.types.EventType> postUpdateEventTypeList = new LinkedList<>();

  ExecutorService executorService = Executors.newCachedThreadPool();

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

  public void fireEvent(Long sleep, common.events.types.EventType eventType) {
    executorService.execute(
        () -> {
          try {
            Thread.sleep(sleep);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          if (this.eventListeners.get(eventType.getType()) != null) {
            this.eventListeners
                .get(eventType.getType())
                .forEach(eventConsumer -> eventConsumer.accept(eventType));
          }
        });
  }

  public void addPostUpdateListener(
      String type, Consumer<common.events.types.EventType> eventConsumer) {
    this.eventPostUpdateListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventPostUpdateListeners.get(type).add(eventConsumer);
  }

  public synchronized void queuePostUpdateEvent(EventType eventType) {
    this.postUpdateEventTypeList.add(eventType);
  }

  public void queuePostUpdateEvent(Long sleep, EventType eventType) {
    executorService.execute(
        () -> {
          try {
            Thread.sleep(sleep);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          this.queuePostUpdateEvent(eventType);
        });
  }

  public synchronized void firePostUpdateEvents() {
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
