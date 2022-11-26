package core.common.events;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;

import core.common.GameSettings;
import core.common.events.types.EventType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;



public class EventService {


  Map<String, List<Consumer<EventType>>> eventListeners = new HashMap<>();
  Map<String, List<Consumer<EventType>>> eventPostUpdateListeners = new HashMap<>();
  ConcurrentLinkedQueue<EventType> postUpdateQueue = new ConcurrentLinkedQueue<>();
  ExecutorService executorService = Executors.newCachedThreadPool();

  @Inject
  public EventService() {}

  public void addListener(String type, Consumer<EventType> eventConsumer) {
    this.eventListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventListeners.get(type).add(eventConsumer);
  }

  public void fireEvent(EventType eventType) {
    if (eventType == null) {
      Gdx.app.debug(GameSettings.LOG_TAG,"eventType is null.");
      return;
    }
    if (this.eventListeners.get(eventType.getEventType()) != null) {
      this.eventListeners
          .get(eventType.getEventType())
          .forEach(eventConsumer -> eventConsumer.accept(eventType));
    }
  }

  public void fireEvent(Long sleep, EventType eventType) {
    executorService.execute(
        () -> {
          try {
            Thread.sleep(sleep);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          if (this.eventListeners.get(eventType.getEventType()) != null) {
            fireEvent(eventType);
          }
        });
  }

  public void addPostUpdateListener(String type, Consumer<EventType> eventConsumer) {
    this.eventPostUpdateListeners.computeIfAbsent(type, k -> new ArrayList<>());
    this.eventPostUpdateListeners.get(type).add(eventConsumer);
  }

  public synchronized void queuePostUpdateEvent(EventType eventType) {
    this.postUpdateQueue.add(eventType);
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
    while (postUpdateQueue.size() > 0) {
      EventType eventType = postUpdateQueue.poll();
      if (this.eventPostUpdateListeners.get(eventType.getEventType()) != null) {
        try {
          this.eventPostUpdateListeners
              .get(eventType.getEventType())
              .forEach(eventConsumer -> eventConsumer.accept(eventType));
        } catch (Exception e) {
          Gdx.app.error(GameSettings.LOG_TAG,("Error with " + eventType), e);
        }
      }
    }
  }
}
