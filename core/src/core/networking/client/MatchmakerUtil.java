package core.networking.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MatchmakerUtil {
  public static String getGameServerAddress(String matchMakerAddress) {
    // for a series of attempts try and get a server from the matchmaker

    OkHttpClient httpClient = new OkHttpClient();
    Request request = new Request.Builder().url(matchMakerAddress).build();

    String gameAddress = null;
    while (gameAddress == null) {
      try (Response response = httpClient.newCall(request).execute()) {
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String x = response.body().string();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        HealthResponse health = gson.fromJson(x, HealthResponse.class);

        System.out.println(health);

        if (health.serverList.size() > 0) gameAddress = health.serverList.get(0);

        Thread.sleep(2000);

      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return gameAddress;
  }
}

class HealthResponse {
  public List<String> serverList;
  public int pendingCount;

  @Override
  public String toString() {
    return "HealthResponse{" + "serverList=" + serverList + ", pendingCount=" + pendingCount + '}';
  }
}
