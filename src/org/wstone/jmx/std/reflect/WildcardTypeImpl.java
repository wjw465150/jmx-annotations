package org.wstone.jmx.std.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;


/**
 * class type matching
 */
public class WildcardTypeImpl extends BaseType implements WildcardType
{
  private BaseType []_lowerBounds;
  private BaseType []_upperBounds;

  public WildcardTypeImpl(BaseType []lowerBounds, BaseType []upperBounds)
  {
    _lowerBounds = lowerBounds;
    _upperBounds = upperBounds;
  }

  @Override
  public Type []getLowerBounds()
  {
    Type []lowerBounds = new Type[_lowerBounds.length];

    for (int i = 0; i < lowerBounds.length; i++)
      lowerBounds[i] = _lowerBounds[i].toType();
      
    return lowerBounds;
  }

  @Override
  public Type []getUpperBounds()
  {
    Type []upperBounds = new Type[_upperBounds.length];

    for (int i = 0; i < upperBounds.length; i++)
      upperBounds[i] = _upperBounds[i].toType();
      
    return upperBounds;
  }

  @Override
  public boolean isWildcard()
  {
    return true;
  }
  
  @Override
  public boolean isGeneric()
  {
    // ioc/07f0
    
    return true;
  }
  
  @Override
  protected BaseType []getWildcardBounds()
  {
    return _upperBounds;
  }
  
  @Override
  public Class<?> getRawClass()
  {
    return Object.class; // technically bounds(?)
  }

  public Type getGenericComponentType()
  {
    return null;
  }

  @Override
  public Type toType()
  {
    return this;
  }

  @Override
  public boolean isAssignableFrom(BaseType type)
  {
    for (BaseType bound : _lowerBounds) {
      if (! type.isAssignableFrom(bound))
        return false;
    }
    
    for (BaseType bound : _upperBounds) {
      if (! bound.isAssignableFrom(type))
        return false;
    }
    
    return true;
  }
  
  @Override
  public boolean isParamAssignableFrom(BaseType type)
  {
    for (BaseType bound : _lowerBounds) {
      if (! type.isAssignableFrom(bound))
        return false;
    }
    
    for (BaseType bound : _upperBounds) {
      if (! bound.isAssignableFrom(type)) {
        return false;
      }
    }
    
    return true;
  }

  @Override
  public int hashCode()
  {
    return 17;
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    else if (o instanceof WildcardType) {
      return true;
    }
    else
      return false;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append("?");
    
    for (BaseType type : _lowerBounds) {
      sb.append(" super ").append(type);
    }
    
    for (BaseType type : _upperBounds) {
      if (! Object.class.equals(type.getRawClass()))
        sb.append(" extends ").append(type);
    }
    
    return sb.toString();
  }
}
