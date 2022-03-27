package networking.ping;

import app.user.UserID;
import com.google.inject.Inject;
import common.GameSettings;
import common.events.EventService;
import java.util.LinkedList;
import java.util.List;
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
  final ConcurrentHashMap<UserID, ConcurrentHashMap<UUID, PingObject>> pingObjectMap =
      new ConcurrentHashMap<>();
  final Logger LOGGER = LogManager.getLogger();
  Timer timer;
  @Inject ConnectionStore connectionStore;
  @Inject EventService eventService;

  public void start() {
    timer = new Timer(true);
    timer.scheduleAtFixedRate(this, 0, GameSettings.PING_INTERVAL);
  }

  public void setResponseTime(UserID userID, UUID pingID, Long responseTime) {
    PingObject pingObject = pingObjectMap.get(userID).get(pingID);
    pingObject.setResponseTime(responseTime);
    LOGGER.info("Received ping: #userID " + userID + " #time " + pingObject.calcPingTime());
  }

  public float getAveragePingTime(UserID userID) {
    List<PingObject> pingObjectList = new LinkedList<>(pingObjectMap.get(userID).values());

    Predicate<PingObject> hasResponse = pingObject -> pingObject.getResponseTime() != null;

    pingObjectList = pingObjectList.stream().filter(hasResponse).collect(Collectors.toList());

    float total = 0;

    for (PingObject pingObject : pingObjectList) {
      total += pingObject.calcPingTime();
    }

    return total / pingObjectList.size();
  }

  @Override
  public void run() {
    for (UserID userID : connectionStore.getConnectedUserID()) {
      pingObjectMap.putIfAbsent(userID, new ConcurrentHashMap<>());
      PingObject pingObject = new PingObject(userID);
      pingObjectMap.get(userID).put(pingObject.getPingID(), pingObject);
      eventService.fireEvent(
          EventTypeFactory.createPingRequestOutgoingEventType(userID, pingObject.getPingID()));
    }
  }
}
