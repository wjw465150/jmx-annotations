package org.wstone.jmx.std.reflect;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * class type matching
 */
public class ObjectType extends ClassType
{
  public static final ObjectType OBJECT_TYPE = new ObjectType();

  protected ObjectType()
  {
    super(Object.class);
  }
  
  @Override
  public Class<?> getRawClass()
  {
    return Object.class;
  }
  
  @Override
  public boolean isObject()
  {
    return true;
  }

  // ioc/024f
  /*
  @Override
  public boolean isParamAssignableFrom(BaseType type)
  {
    return true;
  }
  */
    
  @Override
  public boolean isAssignableFrom(BaseType type)
  {
    return true;
  }
  
  @Override
  public void fillTypeClosure(BaseTypeFactory manager, Set<Type> typeSet)
  {
    typeSet.add(Object.class);
  }

  @Override
  public int hashCode()
  {
    return Object.class.hashCode();
  }

  @Override
  public boolean equals(Object o)
  {
    return (o == this);
  }

  @Override
  public String toString()
  {
    return getRawClass().getName();
  }
}
