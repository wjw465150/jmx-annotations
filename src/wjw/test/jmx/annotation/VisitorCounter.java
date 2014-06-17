package wjw.test.jmx.annotation;

import org.wstone.jmx.*;

@MBean(description = "���ʼ�����",objectName="com.aws.jmx:type=VisitorCounter")
public class VisitorCounter {

  @ManagedAttribute(description = "���ʴ���", writable = false, readable = true)
  private int visits;

  @ManagedOperation(description = "���÷��ʴ���", impact = Impact.WRITE)
  public void resetVisits() {
    this.visits = 0;
  }

  @ManagedOperation(description = "�����������÷��ʴ���", impact = Impact.WRITE)
  public void resetVisits(@Description("����") int visits) {
    this.visits = visits;
  }

  @ManagedOperation(description = "�ۼӷ��ʴ���", impact = Impact.READ_WRITE)
  public int incVisits() {
    return ++this.visits;
  }

  @ManagedOperation(description = "���÷��ʴ���", impact = Impact.WRITE)
  public void setVisits(@Description("����") int visits) {
    this.visits = visits;
  }

  public int getVisits() {
    return visits;
  }

}