package org.wstone.jmx.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

/**
 * @author German Escobar,wjw465150
 * 
 * @param <T>
 * 
 *          The implementation of DynamicMBean. Uses the {@link ExposedMembers}
 *          to retrieve the exposed fields and operations of the instance.
 */
public class MBeanImpl<T> implements DynamicMBean {

  private T implementation;

  private java.util.Map<String, Field> exposedFields;

  private java.util.Map<String, Method> exposedMethods;

  private MBeanInfo mBeanInfo;

  public MBeanImpl(T implementation, Field[] exposedFields, Method[] exposedMethods,
      MBeanInfo mBeanInfo) {
    this.implementation = implementation;
    this.exposedFields = new java.util.HashMap<String, Field>(exposedFields.length);
    this.exposedMethods = new java.util.HashMap<String, Method>(exposedMethods.length);
    this.mBeanInfo = mBeanInfo;

    for (Field field : exposedFields) { //@wjw_add
      field.setAccessible(true);
      this.exposedFields.put(field.getName(), field);
    }
    for (Method method : exposedMethods) { //@wjw_add
      method.setAccessible(true);
      this.exposedMethods.put(method.getName() + Arrays.deepToString(method.getParameterTypes()), method);
    }
  }

  @Override
  public Object getAttribute(String attributeName) throws AttributeNotFoundException,
      MBeanException, ReflectionException {
    // check attribute_name is not null to avoid NullPointerException later on
    if (attributeName == null) {
      throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke a getter of " + mBeanInfo.getClassName() + " with null attribute name");
    }

    try {
      Field f = exposedFields.get(attributeName);
      if (f != null) {
        return f.get(implementation);
      }
    } catch (IllegalAccessException e) {
      return new ReflectionException(e);
    }

    // if attribute_name has not been recognized throw an AttributeNotFoundException
    throw (new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + mBeanInfo.getClassName()));
  }

  @Override
  public AttributeList getAttributes(String[] attributesNames) {
    // Check attributeNames is not null to avoid NullPointerException later on
    if (attributesNames == null) {
      throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"), "Cannot invoke a getter of " + mBeanInfo.getClassName());
    }
    AttributeList resultList = new AttributeList();

    // if attributeNames is empty, return an empty result list
    if (attributesNames.length == 0)
      return resultList;

    // build the result attribute list
    for (int i = 0; i < attributesNames.length; i++) {
      try {
        Object value = getAttribute((String) attributesNames[i]);
        resultList.add(new Attribute(attributesNames[i], value));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return resultList;
  }

  @Override
  public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException,
      MBeanException, ReflectionException {
    // check attribute is not null to avoid NullPointerException later on
    if (attribute == null) {
      throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Cannot invoke a setter of " + mBeanInfo.getClassName() + " with null attribute");
    }

    String name = attribute.getName();
    Object value = attribute.getValue();

    if (name == null) {
      throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke the setter of " + mBeanInfo.getClassName() + " with null attribute name");
    }
    if (value == null) {
      throw (new InvalidAttributeValueException("Cannot set attribute " + name + " to null"));
    }

    Field f = exposedFields.get(name);
    if (f != null) {
      if (isAssignable(f.getType(), value.getClass())) {
        try {
          f.set(implementation, value);
        } catch (IllegalAccessException e) {
          throw new ReflectionException(e);
        }
      } else {
        throw (new InvalidAttributeValueException("Cannot set attribute " + name + " to a " + value.getClass().getName() + " object, " + f.getType().getName() + " expected"));
      }

    } else {
      // no attributes for this class, throw a AttributeNotFoundException
      throw (new AttributeNotFoundException("Attribute " + name + " not found in " + mBeanInfo.getClassName()));
    }
  }

  @Override
  public AttributeList setAttributes(AttributeList attributes) {
    // check attributes is not null to avoid NullPointerException later on
    if (attributes == null) {
      throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"),
          "Cannot invoke a setter of " + mBeanInfo.getClassName());
    }

    AttributeList resultList = new AttributeList();

    // if attributeNames is empty, nothing more to do
    if (attributes.isEmpty())
      return resultList;

    // for each attribute, try to set it and add to the result list if successful
    for (Iterator<Object> i = attributes.iterator(); i.hasNext();) {
      Attribute attr = (Attribute) i.next();
      try {
        setAttribute(attr);
        String name = attr.getName();
        Object value = getAttribute(name);
        resultList.add(new Attribute(name, value));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return resultList;
  }

  @Override
  public Object invoke(String actionName, Object[] params, String[] signature)
      throws MBeanException, ReflectionException {
    // check operationName is not null to avoid NullPointerException later on
    if (actionName == null) {
      throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"),
          "Cannot invoke a null operation in " + mBeanInfo.getClassName());
    }

    // check for a recognized operation name and call the corresponding operation
    Method m = exposedMethods.get(actionName + Arrays.deepToString(signature));
    if (m != null) {
      try {
        return m.invoke(implementation, params);
      } catch (Exception e) {
        throw new ReflectionException(e);
      }
    } else {
      // unrecognized operation name:
      throw new ReflectionException(new NoSuchMethodException(actionName),
          "Cannot find the operation " + actionName + " in " + mBeanInfo.getClassName());
    }
  }

  @Override
  public MBeanInfo getMBeanInfo() {
    return mBeanInfo;
  }

  private boolean isAssignable(Class<?> to, Class<?> from) {
    if (to.isPrimitive()) {
      to = fromPrimitiveToObject(to);
    }

    if (from.isPrimitive()) {
      from = fromPrimitiveToObject(from);
    }

    return to.isAssignableFrom(from);
  }

  private Class<?> fromPrimitiveToObject(Class<?> primitive) {
    if (primitive.equals(Integer.TYPE)) {
      return Integer.class;
    }

    return primitive;
  }

}
