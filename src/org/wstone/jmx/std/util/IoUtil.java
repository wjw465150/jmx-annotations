package org.wstone.jmx.std.util;

import java.util.logging.*;
import java.io.*;

/**
 * convenience methods for io
 */
public class IoUtil {
  private static final Logger log
    = Logger.getLogger(IoUtil.class.getName());

  public static void close(InputStream is)
  {
    try {
      if (is != null)
        is.close();
    } catch (IOException e) {
      log.log(Level.FINER, e.toString(), e);
    }
  }

  public static void close(OutputStream os)
  {
    try {
      if (os != null)
        os.close();
    } catch (IOException e) {
      log.log(Level.FINER, e.toString(), e);
    }
  }

  public static void close(Writer os)
  {
    try {
      if (os != null)
        os.close();
    } catch (IOException e) {
      log.log(Level.FINER, e.toString(), e);
    }
  }
} 