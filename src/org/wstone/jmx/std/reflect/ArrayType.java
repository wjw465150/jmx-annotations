package org.wstone.jmx.std.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * class type matching
 */
public class ArrayType extends BaseType implements GenericArrayType
{
  private BaseType _componentType;
  private Class<?> _rawType;

  public ArrayType(BaseType componentType, Class<?> rawType)
  {
    _componentType = componentType;
    _rawType = rawType;
  }
  
  @Override
  public Class<?> getRawClass()
  {
    return _rawType;
  }

  @Override
  public Type getGenericComponentType()
  {
    return _componentType.toType();
  }

  @Override
  public Type toType()
  {
    return this;
  }
  
  @Override
  public boolean isParamAssignableFrom(BaseType type)
  {
    if (type instanceof ArrayType) {
      ArrayType aType = (ArrayType) type;

      return getGenericComponentType().equals(aType.getGenericComponentType());
    }
    else
      return false;
  }

  @Override
  protected void fillTypeClosure(BaseTypeFactory manager, Set<Type> typeSet)
  {
    typeSet.add(toType());
    typeSet.add(Object.class);
  }
  
  @Override
  public boolean isAssignableFrom(BaseType type)
  {
    return equals(type);
  }

  @Override
  public int hashCode()
  {
    return 17 + 37 * getGenericComponentType().hashCode();
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    else if (o instanceof GenericArrayType) {
      GenericArrayType type = (GenericArrayType) o;

      return getGenericComponentType().equals(type.getGenericComponentType());
    }
    else
      return false;
  }

  @Override
  public String getSimpleName()
  {
    return _componentType.getSimpleName() + "[]";
  }

  @Override
  public String toString()
  {
    return _componentType + "[]";
  }
}
