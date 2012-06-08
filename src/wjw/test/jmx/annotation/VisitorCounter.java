package wjw.test.jmx.annotation;

import org.wstone.jmx.*;

@MBean
@Description("访问计数类")
public class VisitorCounter {

  @ManagedAttribute(writable = false, readable = true)
  @Description("访问次数")
  private int visits;

  @ManagedOperation(impact = Impact.WRITE)
  @Description("重置访问次数")
  public void resetVisits() {
    this.visits = 0;
  }

  @ManagedOperation(impact = Impact.WRITE)
  @Description("带参数的重置访问次数")
  public void resetVisits(@Description("次数") int visits) {
    this.visits = visits;
  }

  @ManagedOperation(impact = Impact.READ_WRITE)
  @Description("累加访问次数")
  public int incVisits() {
    return ++this.visits;
  }

  @ManagedOperation(impact = Impact.WRITE)
  @Description("设置访问次数")
  public void setVisits(@Description("次数") int visits) {
    this.visits = visits;
  }

  public int getVisits() {
    return visits;
  }

}