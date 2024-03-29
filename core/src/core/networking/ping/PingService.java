package core.networking.ping;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import core.app.user.UserID;
import core.common.Clock;
import core.common.GameSettings;
import core.common.events.EventService;
import core.networking.ConnectionStore;
import core.networking.events.EventTypeFactory;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PingService extends TimerTask {
  final ConcurrentHashMap<UserID, LinkedHashMap<UUID, PingObject>> pingObjectMap =
      new ConcurrentHashMap<>();

  Timer timer;
  @Inject ConnectionStore connectionStore;
  @Inject EventService eventService;

  public void start() {
    timer = new Timer(true);
    timer.scheduleAtFixedRate(this, 0, GameSettings.PING_INTERVAL);
  }

  public void setResponseTime(UserID userID, UUID pingID, Long responseTime, Long receivedTime) {
    PingObject pingObject = pingObjectMap.get(userID).get(pingID);
    pingObject.setResponseTime(responseTime);
    pingObject.setReceiverTime(receivedTime);

    Gdx.app.debug(
        GameSettings.LOG_TAG,
        "Received ping: #userID " + userID + " #time " + pingObject.calcPingTime());
  }

  public long calcDelay(UserID userID, Long theirTime) {
    long ourTime = Clock.getCurrentTime();
    long avgPing;
    long avgDiff;
    try {
      List<PingObject> pingObjectList =
          new LinkedList<>(pingObjectMap.getOrDefault(userID, new LinkedHashMap<>()).values());

      Predicate<PingObject> hasResponse =
          pingObject ->
              pingObject.getResponseTime() != null && pingObject.getReceiverTime() != null;

      pingObjectList = pingObjectList.stream().filter(hasResponse).collect(Collectors.toList());

      long totalPing = 0;
      long totalDiff = 0;

      for (PingObject pingObject : pingObjectList) {
        totalPing += pingObject.calcPingTime();
        totalDiff += pingObject.getResponseTime() - pingObject.getReceiverTime();
      }

      if (pingObjectList.size() == 0) return 0;

      avgPing = totalPing / pingObjectList.size();
      avgDiff = totalDiff / pingObjectList.size();
    } catch (Exception e) {
      Gdx.app.error(GameSettings.LOG_TAG, e.getMessage(), e);
      return 0;
    }
    long delay = theirTime + avgDiff - ourTime + (long) (avgPing * .1);
    return delay;
  }

  @Override
  public void run() {
    for (UserID userID : connectionStore.getConnectedUserID()) {
      pingObjectMap.putIfAbsent(
          userID,
          new LinkedHashMap<UUID, PingObject>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
              return size() > GameSettings.PING_LIMIT;
            }
          });
      PingObject pingObject = new PingObject(userID);
      pingObjectMap.get(userID).put(pingObject.getPingID(), pingObject);
      eventService.fireEvent(
          EventTypeFactory.createPingRequestOutgoingEventType(userID, pingObject.getPingID()));
    }
  }
}
