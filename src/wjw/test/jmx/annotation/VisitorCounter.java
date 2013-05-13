package wjw.test.jmx.annotation;

import org.wstone.jmx.*;

@MBean(description = "访问计数类",objectName="com.aws.jmx:type=VisitorCounter")
public class VisitorCounter {

  @ManagedAttribute(description = "访问次数", writable = false, readable = true)
  private int visits;

  @ManagedOperation(description = "重置访问次数", impact = Impact.WRITE)
  public void resetVisits() {
    this.visits = 0;
  }

  @ManagedOperation(description = "带参数的重置访问次数", impact = Impact.WRITE)
  public void resetVisits(@Description("次数") int visits) {
    this.visits = visits;
  }

  @ManagedOperation(description = "累加访问次数", impact = Impact.READ_WRITE)
  public int incVisits() {
    return ++this.visits;
  }

  @ManagedOperation(description = "设置访问次数", impact = Impact.WRITE)
  public void setVisits(@Description("次数") int visits) {
    this.visits = visits;
  }

  public int getVisits() {
    return visits;
  }

}