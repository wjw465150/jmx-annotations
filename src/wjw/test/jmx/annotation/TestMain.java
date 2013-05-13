package wjw.test.jmx.annotation;

import javax.management.DynamicMBean;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.wstone.jmx.MBeanFactory;
import org.wstone.jmx.std.StandardMBeanFactory;
import org.wstone.jmx.util.MBeanServerLocator;

public class TestMain {
  public static void main(String[] args) {
    try {
      MBeanFactory mBeanFactory = new StandardMBeanFactory();

      // create the MBean
      VisitorCounter visitorCounterA = new VisitorCounter();
      DynamicMBean mBean = mBeanFactory.createMBean(visitorCounterA);

      // find MBeanServer
      MBeanServer ms = MBeanServerLocator.instance().getMBeanServer();

      // register the MBean
      ms.registerMBean(mBean, new ObjectName("org.test:type=VisitorCounter"));
      ms.registerMBean(mBean, null);  //由MBeanImpl自动提供ObjectName
      
      User user = new User("一枝花",18);
      DynamicMBean mBeanB = mBeanFactory.createMBean(user);
      ms.registerMBean(mBeanB, new ObjectName("org.test:type=User"));
      
      System.out.println("started MBeanServer...");

      for (;;) {
        try {
          java.util.concurrent.TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          System.exit(0);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
