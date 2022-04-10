package networking.ping;

import app.user.UserID;
import java.util.UUID;

public class PingObject {
  private final UUID pingID = UUID.randomUUID();
  private final Long requestTime = System.currentTimeMillis();
  private final UserID userID;
  private Long responseTime;
  private Long receiverTime;

  public PingObject(UserID userID) {
    this.userID = userID;
  }

  public Long getReceiverTime() {
    return receiverTime;
  }

  public void setReceiverTime(Long receiverTime) {
    this.receiverTime = receiverTime;
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

  public long calcPingTime() {
    return (this.getResponseTime() - this.getRequestTime()) / 2;
  }
}
