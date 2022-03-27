package networking.ping;

import app.user.UserID;
import java.util.UUID;

public class PingObject {
  private UserID userID;
  private UUID pingID = UUID.randomUUID();
  private Long requestTime = System.currentTimeMillis();
  private Long responseTime;

  public PingObject(UserID userID) {
    this.userID = userID;
  }

  public UUID getPingID() {
    return pingID;
  }

  public Long getRequestTime() {
    return requestTime;
  }

  public Long getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(long responseTime) {
    this.responseTime = responseTime;
  }

  public float calcPingTime() {
    return (this.getResponseTime() - this.getRequestTime()) / 1000f;
  }
}
