package org.wstone.jmx.cdi;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;

import org.wstone.jmx.Impact;
import org.wstone.jmx.ManagedAttribute;
import org.wstone.jmx.ManagedOperation;

/**
 * @author German Escobar
 * 
 *         A DynamicMBeanInfoBuilder is a {@link StandardMBeanInfoBuilder} that
 *         only includes those fields and methods annotated with
 *         {@link org.wstone.jmx.ManagedAttribute} and
 *         {@link org.wstone.jmx.ManagedOperation} in the MBeanInfo.
 *         The AnnotatedType must be annotated with
 *         {@link org.wstone.jmx.MBean}.
 */
public class DynamicMBeanInfoBuilder extends StandardMBeanInfoBuilder {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.wstone.jmx.cdi.AnnotatedTypeVisitor#visitAnnotatedField(javax
   * .enterprise.inject.spi.AnnotatedField)
   */
  @Override
  public <T> void visitAnnotatedField(AnnotatedField<T> af) {
    // if the annotation is not present, ignore the field
    if (!af.isAnnotationPresent(ManagedAttribute.class)) {
      return;
    }

    ManagedAttribute annAttribute = af.getAnnotation(ManagedAttribute.class);
    boolean readable = annAttribute.readable();
    boolean writable = annAttribute.writable();

    super.visitAnnotatedField(af, readable, writable);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.wstone.jmx.cdi.AnnotatedTypeVisitor#visitAnnotatedMethod(javax
   * .enterprise.inject.spi.AnnotatedMethod)
   */
  @Override
  public <T> void visitAnnotatedMethod(AnnotatedMethod<T> am) {
    // if the annotation is not present, ignore the method
    if (!am.isAnnotationPresent(ManagedOperation.class)) {
      return;
    }

    ManagedOperation annOperation = am.getAnnotation(ManagedOperation.class);
    Impact impact = annOperation.impact();

    super.visitAnnotatedMethod(am, impact);

  }

}
