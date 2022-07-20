package core.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.tools.javac.util.Pair;
import core.entity.attributes.msc.Coordinates;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonFactory {
  static final Logger LOGGER = LogManager.getLogger();
  static LoadingCache<Pair<Float, Float>, Coordinates> coordinatesCache =
      CacheBuilder.newBuilder()
          .maximumSize(10000)
          .expireAfterWrite(10, TimeUnit.MINUTES)
          .build(
              new CacheLoader<Pair<Float, Float>, Coordinates>() {
                public Coordinates load(Pair<Float, Float> pair) {
                  return new Coordinates(pair.fst, pair.snd);
                }
              });

  public static Coordinates createCoordinates(float x, float y) {
    try {
      return coordinatesCache.get(new Pair<>(x, y));
    } catch (ExecutionException e) {
      LOGGER.error("Failed to load Coordinates cache.", e);
      return new Coordinates(x, y);
    }
  }
}
