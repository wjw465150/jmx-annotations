package org.wstone.jmx.std;

import javax.enterprise.inject.spi.AnnotatedType;

import org.wstone.jmx.cdi.AbstractMBeanFactory;
import org.wstone.jmx.std.reflect.ReflectionAnnotatedFactory;

public class StandardMBeanFactory extends AbstractMBeanFactory {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.wstone.jmx.cdi.AbstractMBeanFactory#getAnnotatedType(java.
   * lang.Object)
   */
  @Override
  protected AnnotatedType<? extends Object> getAnnotatedType(Object instance) throws Exception {
    return ReflectionAnnotatedFactory.introspectType(instance.getClass());
  }

}
