package org.wstone.jmx.std.reflect;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * class type matching
 */
public class BoxType extends ClassType
{
  private Class<?> _boxType;

  public BoxType(Class<?> type, Class<?> boxType)
  {
    super(type);
    
    _boxType = boxType;
  }
  
  @Override
  public Class<?> getRawClass()
  {
    return _boxType;
  }

  @Override
  public boolean isParamAssignableFrom(BaseType type)
  {
    if (_boxType.equals(type.getRawClass()))
      return true;
    else if (type.isWildcard())
      return true;
    else
      return false;
  }
    
  @Override
  public boolean isAssignableFrom(BaseType type)
  {
    if (! _boxType.isAssignableFrom(type.getRawClass()))
      return false;
    else if (type.getParameters().length > 0) {
      for (BaseType param : type.getParameters()) {
        if (! OBJECT_TYPE.isParamAssignableFrom(param))
          return false;
      }

      return true;
    }
    else
      return true;
  }

  @Override
  public boolean isPrimitive()
  {
    return true;
  }
  
  @Override
  public void fillTypeClosure(BaseTypeFactory manager, Set<Type> typeSet)
  {
    typeSet.add(toType());
    typeSet.add(Object.class);
  }

  @Override
  public int hashCode()
  {
    return _boxType.hashCode();
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    else if (! (o instanceof BoxType))
      return false;

    BoxType type = (BoxType) o;

    return _boxType.equals(type._boxType);
  }

  @Override
  public String toString()
  {
    return super.getRawClass().getName();
  }
}
