package org.wstone.jmx.cdi;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

/**
 * @author German Escobar
 * 
 *         This class is a concrete {@link org.wstone.jmx.MBeanFactory}
 *         used to create MBeans that can be registered manually to a
 *         MBeanServer. It can be injected in any CDI bean.
 */
public class CDIMBeanFactory extends AbstractMBeanFactory {

  @Inject
  private BeanManager beanManager;

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.wstone.jmx.cdi.AbstractMBeanFactory#getAnnotatedType(java.
   * lang.Object)
   */
  @Override
  protected AnnotatedType<? extends Object> getAnnotatedType(Object instance)
      throws Exception {

    return beanManager.createAnnotatedType(instance.getClass());
  }

}
