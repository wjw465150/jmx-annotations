package org.wstone.jmx.cdi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.wstone.jmx.Description;
import org.wstone.jmx.Impact;
import org.wstone.jmx.MBean;
import org.wstone.jmx.ManagedAttribute;
import org.wstone.jmx.ManagedOperation;

/**
 * @author German Escobar
 * 
 */
public class StandardMBeanInfoBuilder implements AnnotatedTypeVisitor {

  /**
   * The name of the class. Used to create the MBeanInfo.
   */
  protected String className = "";

  /**
   * The description of the MBean. Used to create the MBeanInfo.
   */
  protected String description = "";

  /**
   * The constructors of the MBean. Used to create the MBeanInfo.
   */
  protected Set<MBeanConstructorInfo> mBeanConstructors = new HashSet<MBeanConstructorInfo>();

  /**
   * The attributes of the MBean. Used to create the MBeanInfo.
   */
  protected Set<MBeanAttributeInfo> mBeanAttributes = new HashSet<MBeanAttributeInfo>();

  /**
   * The operations of the MBean. Used to create the MBeanInfo.
   */
  protected Set<MBeanOperationInfo> mBeanOperations = new HashSet<MBeanOperationInfo>();

  /**
   * The attributes that will be exposed in the MBean.
   */
  protected Set<Field> exposedFields = new HashSet<Field>();

  /**
   * The methods that will be exposed in the MBean.
   */
  protected Set<Method> exposedMethods = new HashSet<Method>();

  /*
   * (non-Javadoc)
   * 
   * @see org.wstone.jmx.cdi.AnnotatedTypeVisitor#visitAnnotatedType(javax
   * .enterprise.inject.spi.AnnotatedType)
   */
  @Override
  public <T> void visitAnnotatedType(AnnotatedType<T> at) {
    this.className = at.getJavaClass().getName();

    // retrieve the description
    if (at.isAnnotationPresent(MBean.class)) {
      MBean annMBean = at.getAnnotation(MBean.class);
      this.description = annMBean.description();
    } else if (at.isAnnotationPresent(Description.class)) {
      Description annDescription = at.getAnnotation(Description.class);
      this.description = annDescription.value();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wstone.jmx.cdi.AnnotatedTypeVisitor#visitAnnotatedConstructor
   * (javax.enterprise.inject.spi.AnnotatedConstructor)
   */
  @Override
  public <T> void visitAnnotatedConstructor(AnnotatedConstructor<T> ac) {
    String consDescription = "";
    if (ac.isAnnotationPresent(Description.class)) {
      Description annDescription = ac.getAnnotation(Description.class);
      consDescription = annDescription.value();
    }

    MBeanConstructorInfo constructorInfo = new MBeanConstructorInfo(consDescription, ac.getJavaMember());
    this.mBeanConstructors.add(constructorInfo);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wstone.jmx.cdi.AnnotatedTypeVisitor#visitAnnotatedField(javax
   * .enterprise.inject.spi.AnnotatedField)
   */
  @Override
  public <T> void visitAnnotatedField(AnnotatedField<T> af) {
    /* FIXME should check if set and get methods exists */
    boolean readable = true;
    boolean writable = true;

    Field field = af.getJavaMember();
    if (Modifier.isFinal(field.getModifiers())) {
      writable = false;
    }

    this.visitAnnotatedField(af, readable, writable);
  }

  protected <T> void visitAnnotatedField(AnnotatedField<T> af, boolean readable, boolean writable) {
    Field field = af.getJavaMember();
    // add the field to the collection of exposed fields
    exposedFields.add(af.getJavaMember());

    // create the MBeanAttributeInfo
    String fieldDescription = "";
    if (af.isAnnotationPresent(ManagedAttribute.class)) {
      ManagedAttribute annManagedAttribute = af.getAnnotation(ManagedAttribute.class);
      fieldDescription = annManagedAttribute.description();
    } else if (af.isAnnotationPresent(Description.class)) {
      Description annDescription = af.getAnnotation(Description.class);
      fieldDescription = annDescription.value();
    }

    MBeanAttributeInfo attributeInfo = new MBeanAttributeInfo(field.getName(), field.getType().getName(),
        fieldDescription, readable, writable, false);

    this.mBeanAttributes.add(attributeInfo);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wstone.jmx.cdi.AnnotatedTypeVisitor#visitAnnotatedMethod(javax
   * .enterprise.inject.spi.AnnotatedMethod)
   */
  @Override
  public <T> void visitAnnotatedMethod(AnnotatedMethod<T> am) {
    Method method = am.getJavaMember();
    if (!am.isAnnotationPresent(ManagedOperation.class)) { //@wjw_note: 对没有注解的方法,只暴露public的.
      if (!Modifier.isPublic(method.getModifiers())) {
        return;
      }
    }

    visitAnnotatedMethod(am, Impact.UNKNOWN);
  }

  protected <T> void visitAnnotatedMethod(AnnotatedMethod<T> am, Impact impact) {
    Method method = am.getJavaMember();
    // add the method to the collection of exposed methods
    exposedMethods.add(am.getJavaMember());

    // create the MBeanOperationInfo
    String methodDescription = "";
    if (am.isAnnotationPresent(ManagedOperation.class)) {
      ManagedOperation annManagedOperation = am.getAnnotation(ManagedOperation.class);
      methodDescription = annManagedOperation.description();
    } else if (am.isAnnotationPresent(Description.class)) {
      Description annDescription = am.getAnnotation(Description.class);
      methodDescription = annDescription.value();
    }

    List<AnnotatedParameter<T>> annotatedParams = am.getParameters();
    Class<?>[] paramsTypes = method.getParameterTypes();
    MBeanParameterInfo[] params = new MBeanParameterInfo[annotatedParams.size()];

    for (AnnotatedParameter<T> ap : annotatedParams) {
      String paramDescription = "";
      if (ap.isAnnotationPresent(Description.class)) {
        Description annDescription = ap.getAnnotation(Description.class);
        paramDescription = annDescription.value();
      }

      int position = ap.getPosition();

      MBeanParameterInfo parameterInfo = new MBeanParameterInfo("param" + position, paramsTypes[position].getName(), paramDescription);
      params[position] = parameterInfo;

    }

    MBeanOperationInfo operationInfo = new MBeanOperationInfo(method.getName(),
        methodDescription, params, method.getReturnType().getName(), impact.getCode());

    mBeanOperations.add(operationInfo);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wstone.jmx.cdi.AnnotatedTypeVisitor#getFields()
   */
  @Override
  public Field[] getFields() {
    return exposedFields.toArray(new Field[exposedFields.size()]);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wstone.jmx.cdi.AnnotatedTypeVisitor#getMethods()
   */
  @Override
  public Method[] getMethods() {
    return exposedMethods.toArray(new Method[exposedMethods.size()]);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.wstone.jmx.cdi.AnnotatedTypeVisitor#getMBeanInfo()
   */
  @Override
  public MBeanInfo getMBeanInfo() {
    return new MBeanInfo(this.className, this.description, getMBeanAttributes(),
        getMBeanConstructors(), getMBeanOperations(), new MBeanNotificationInfo[0]);
  }

  private MBeanConstructorInfo[] getMBeanConstructors() {
    return this.mBeanConstructors.toArray(new MBeanConstructorInfo[mBeanConstructors.size()]);
  }

  private MBeanAttributeInfo[] getMBeanAttributes() {
    return this.mBeanAttributes.toArray(new MBeanAttributeInfo[mBeanAttributes.size()]);
  }

  private MBeanOperationInfo[] getMBeanOperations() {
    return this.mBeanOperations.toArray(new MBeanOperationInfo[mBeanOperations.size()]);
  }

}
