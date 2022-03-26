package logger;

import org.apache.log4j.Logger;
import org.junit.Test;

public class testLogger {

  @Test
  public void testLogger() {
    Logger logger = Logger.getLogger(testLogger.class);

    if (logger.isDebugEnabled()) {
      logger.debug("This is debug log..");
    }

    if (logger.isInfoEnabled()) {
      logger.info("This is info  log ...");
    }

    logger.warn("This is warn log ...");
    logger.error("This is error log... ");
    logger.fatal("This is fatal log ...");
  }
}
