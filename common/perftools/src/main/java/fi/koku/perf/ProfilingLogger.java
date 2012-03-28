package fi.koku.perf;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * log method entry and exit.
 * 
 * @author aspluma
 */
public class ProfilingLogger {
  private static Logger logger = LoggerFactory.getLogger(ProfilingLogger.class);
  
  public static void before(JoinPoint tjp) {
    logger.debug("PROF: start: " + tjp.getStaticPart().getSignature().toLongString());
  }
  
  public static void after(JoinPoint tjp) {
    logger.debug("PROF: end: "+ tjp.getStaticPart().getSignature().toShortString());
  }
  
}
