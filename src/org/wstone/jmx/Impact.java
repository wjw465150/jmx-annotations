package org.wstone.jmx;

import javax.management.MBeanOperationInfo;

public enum Impact {

  READ(MBeanOperationInfo.ACTION),

  WRITE(MBeanOperationInfo.INFO),

  READ_WRITE(MBeanOperationInfo.ACTION_INFO),

  UNKNOWN(MBeanOperationInfo.UNKNOWN);

  private int code;

  private Impact(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

}
