package core.common;

import com.badlogic.gdx.Gdx;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GameSettings {
  public static final boolean RENDER_DEBUG = false;

  public static final String LOG_TAG = "My log";

  public static final int AI_LIMIT = 10;

  public static final int UPDATE_INTERVAL = 15;
  public static final float WORLD_TIME_STEP = 1 / 5f;
  public static final int WORLD_VELOCITY_ITERATIONS = 6;
  public static final int WORLD_POSITION_ITERATIONS = 2;
  public static final int CHUNK_SIZE = 5;
  public static final int PIXEL_SCALE = 70;
  public static final int PHYSICS_SCALE = 25;
  public static final float GRAVITY = 1f;
  public static final int GENERATION_THREADS = 5;
  public static final int PATHING_THREADS = 7;
  public static final int PING_LIMIT = 20;
  public static final int PING_INTERVAL = 5000;

  public static final int HANDSHAKE_TIMEOUT = 75;
  public static final Integer GCD_TIMEOUT = 50;
  private static final String HOST_KEY = "host";
  private static final String PORT_KEY = "port";
  private static final String IS_MATCHMAKER = "matchmaker";
  private Properties properties;
  private String VERSION = null;

  public GameSettings() {}

  private Properties getDefaults() {
    Properties defaults = new Properties();
    defaults.setProperty(HOST_KEY, "https://solarsim.net");
    defaults.setProperty(PORT_KEY, "99");
    defaults.setProperty(IS_MATCHMAKER, "1");
    return defaults;
  }

  public void load() {
    if (properties != null) return;
    properties = this.getDefaults();
    File configFile = new File(System.getProperty("user.dir") + "/config.properties");
    if (configFile.exists() && !configFile.isDirectory() && configFile.canRead()) {
      try {
        properties.load(new FileReader(configFile));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      // TODO log message cannot read
    }
    try {
      properties.store(new FileOutputStream(configFile), null);
    } catch (IOException e) {
      e.printStackTrace();
    }
    VERSION = Gdx.files.internal("project.version").readString();
  }

  private synchronized String getValue(String key) {
    this.load();
    return properties.getProperty(key);
  }

  public String getHost() {
    return this.getValue(HOST_KEY);
  }

  public int getPort() {
    return Integer.parseInt(this.getValue(PORT_KEY));
  }

  public boolean isMatchMakerAddress() {
    return this.getValue(IS_MATCHMAKER).equals("1");
  }

  public String getVersion() {
    this.load();
    return this.VERSION;
  }
}
