package networking.ping;

import app.user.UserID;
import com.google.inject.Inject;
import common.Clock;
import common.GameSettings;
import common.events.EventService;
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
import networking.ConnectionStore;
import networking.events.EventTypeFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PingService extends TimerTask {
  final ConcurrentHashMap<UserID, LinkedHashMap<UUID, PingObject>> pingObjectMap =
      new ConcurrentHashMap<>();
  final Logger LOGGER = LogManager.getLogger();
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

    LOGGER.info("Received ping: #userID " + userID + " #time " + pingObject.calcPingTime());
  }

  public long calcDelay(UserID userID, Long theirTime) {
    long ourTime = Clock.getCurrentTime();
    long avgPing;
    long avgDiff;
    try {
      List<PingObject> pingObjectList = new LinkedList<>(pingObjectMap.get(userID).values());

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

      avgPing = totalPing / pingObjectList.size();
      avgDiff = totalDiff / pingObjectList.size();
    } catch (Exception e) {
      LOGGER.error(e);
      return 0;
    }
    long delay = theirTime + avgDiff - ourTime + (long) (avgPing * .1);
    LOGGER.info("delay : " + delay);
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
