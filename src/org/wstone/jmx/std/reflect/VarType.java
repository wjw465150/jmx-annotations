package org.wstone.jmx.std.reflect;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Set;


/**
 * class type matching
 */
public class VarType<D extends GenericDeclaration> extends BaseType
  implements TypeVariable<D>
{
  private String _name;
  private BaseType []_bounds;

  public VarType(String name, BaseType []bounds)
  {
    _name = name;
    _bounds = bounds;
  }

  @Override
  public String getName()
  {
    return _name;
  }

  @Override
  public D getGenericDeclaration()
  {
    return (D) new GenericDeclarationImpl();
  }

  @Override
  public boolean isWildcard()
  {
    // ioc/024j vs ioc/024k
    return false;
  }
  
  @Override
  public boolean isGeneric()
  {
    return true;
  }
  
  @Override
  public boolean isVariable()
  {
    return true;
  }

  @Override
  public Type []getBounds()
  {
    Type []bounds = new Type[_bounds.length];

    for (int i = 0; i < bounds.length; i++) {
      bounds[i] = _bounds[i].toType();
    }

    return bounds;
  }
  
  @Override
  protected BaseType []getWildcardBounds()
  {
    return _bounds;
  }
  
  @Override
  public Class<?> getRawClass()
  {
    return Object.class; // technically bounds
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
    if (type.isWildcard())
      return true;
    
    for (BaseType bound : _bounds) {
      if (! bound.isAssignableFrom(type)) {
        return false;
      }
    }
    
    return true;
  }
  
  @Override
  public boolean isParamAssignableFrom(BaseType type)
  {
    // ioc/0i3m
    return isAssignableFrom(type);
  }

  @Override
  public void fillSyntheticTypes(Set<VarType<?>> varTypeList)
  {
    varTypeList.add(this);
  }

  @Override
  public int hashCode()
  {
    return 17 + 37 * _name.hashCode();
  }

  @Override
  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    else if (o instanceof TypeVariable<?>) {
      // TypeVariable<?> var = (TypeVariable<?>) o;

      return true;
    }
    else
      return false;
  }

  public String toString()
  {
    if (_bounds.length == 0)
      return _name;
    
    StringBuilder sb = new StringBuilder(_name);
    
    for (BaseType type : _bounds) {
      if (! type.getRawClass().equals(Object.class))
        sb.append(" extends ").append(type);
    }
    
    return sb.toString();
  }
  
  static class GenericDeclarationImpl implements GenericDeclaration {
    @Override
    public TypeVariable<?>[] getTypeParameters()
    {
      return null;
    }
    
  }
}
