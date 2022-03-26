package logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class testLogger {
  final Logger LOGGER = LogManager.getLogger(testLogger.class);

  @Test
  public void testLogger() {

    LOGGER.debug("This is an DEBUG level log message!");
    LOGGER.info("This is an INFO level log message!");
    LOGGER.error("This is an ERROR level log message!");
  }
}
