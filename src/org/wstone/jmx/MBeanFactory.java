package org.wstone.jmx;

import javax.management.DynamicMBean;

/**
 * @author German Escobar
 * 
 * 
 */
public interface MBeanFactory {

  /**
   * Creates a {@link javax.management.DynamicMBean} based on the object
   * received as an argument. If the object is annotated with the
   * {@link org.wstone.jmx.annotations.MBean} annotation, only the
   * fields and methods annotated with
   * {@link org.wstone.jmx.annotations.ManagedAttribute} and
   * {@link org.wstone.jmx.annotations.ManagedOperation} will be
   * exposed. Otherwise, all fields and methods will be exposed.
   * 
   * @param instance
   *          the object that wants to be exposed as an MBean.
   * @return the {@link javax.management.DynamicMBean} that can be registered to
   *         an MBeanServer.
   * @throws ManagementException
   *           wraps any exception (checked or unchecked) that is thrown in the
   *           creation process.
   */
  <T> DynamicMBean createMBean(T instance) throws ManagementException;

}
