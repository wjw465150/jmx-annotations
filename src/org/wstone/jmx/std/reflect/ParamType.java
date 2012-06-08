package org.wstone.jmx.std.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * param type matching
 */
public class ParamType extends BaseType implements ParameterizedType
{
  private Class<?> _type;
  private BaseType []_param;
  private Type []_actualArguments;
  private HashMap<String,BaseType> _paramMap;

  public ParamType(Class<?> type,
                   BaseType []param,
                   HashMap<String,BaseType> paramMap)
  {
    _type = type;
    _param = param;
    _paramMap = paramMap;

    _actualArguments = new Type[param.length];
    for (int i = 0; i < param.length; i++) {
      _actualArguments[i] = param[i].toType();
    }
    
    /*
    if (type.equals(Set.class)) {
      // System.out.println("PTYPE: " + this + " " + type + " " + paramMap);
      // Thread.dumpStack();
    }
    */
  }
  
  @Override
  public Class<?> getRawClass()
  {
    return _type;
  }

  @Override
  public Type toType()
  {
    return this;
  }

  @Override
  public Type []getActualTypeArguments()
  {
    return _actualArguments;
  }

  @Override
  public Type getOwnerType()
  {
    return null;
  }
  
  @Override
  public BaseType []getParameters()
  {
    return _param;
  }
  
  @Override
  public boolean isGeneric()
  {
    // ioc/07f0
    for (BaseType type : _param) {
      if (type.isGeneric())
        return true;
    }
    
    return false;
  }
  
  @Override
  public boolean isGenericVariable()
  {
    // ioc/0190, ioc/0192
    for (BaseType type : _param) {
      if (type.isVariable())
        return true;
    }
    
    return false;
  }

  @Override
  public HashMap<String,BaseType> getParamMap()
  {
    return _paramMap;
  }

  @Override
  public Type getRawType()
  {
    return _type;
  }

  @Override
  protected void fillTypeClosure(BaseTypeFactory manager, Set<Type> typeSet)
  {
    typeSet.add(toType());
    
    for (Type type : _type.getGenericInterfaces()) {
      BaseType ifaceType = createForSource(type, _paramMap, null);
      
      ifaceType.fillTypeClosure(manager, typeSet);
    }

    Type superclass = _type.getGenericSuperclass();
    
    if (superclass == null)
      return;

    BaseType superType = createForSource(superclass, _paramMap, null);

    superType.fillTypeClosure(manager, typeSet);
  }
  
  @Override
  public BaseType fill(BaseType ...types)
  {
    TypeVariable []vars = _type.getTypeParameters();
    
    if (vars.length != types.length)
      throw new IllegalStateException();
    
    HashMap<String,BaseType> paramMap = new HashMap<String,BaseType>(_paramMap);

    for (int i = 0; i < vars.length; i++) {
      TypeVariable<Class<?>> var = vars[i];
      
      paramMap.put(var.getName(), types[i]);
    }
    
    return new ParamType(_type, types, paramMap);
  }
  
  @Override
  public void fillSyntheticTypes(Set<VarType<?>> varTypeList)
  {
    for (BaseType param : _param) {
      param.fillSyntheticTypes(varTypeList);
    }
  }

  @Override
  public boolean isAssignableFrom(BaseType type)
  {
    // ioc/0062
    if (! getRawClass().isAssignableFrom(type.getRawClass()))
      return false;
    
    if (! getRawClass().equals(type.getRawClass())) {
      // ioc/0pb0
      return false;
    }
    
    BaseType []paramA = getParameters();
    BaseType []paramB = type.getParameters();

    if (paramA.length != paramB.length)
      return false;

    for (int i = 0; i < paramA.length; i++) {
      if (! paramA[i].isParamAssignableFrom(paramB[i])) {
        return false;
      }
    }

    return true;
  }
  
  @Override
  public boolean isParamAssignableFrom(BaseType type)
  {
    if (! (type instanceof ParamType))
      return false;

    ParamType pType = (ParamType) type;
    Type rawType = pType.getRawType();

    if (! _type.equals(rawType))
      return false;

    BaseType []paramA = getParameters();
    BaseType []paramB = type.getParameters();

    if (paramA.length != paramB.length)
      return false;

    for (int i = 0; i < paramA.length; i++) {
      if (! paramA[i].isParamAssignableFrom(paramB[i])) {
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return _type.hashCode() ^ Arrays.hashCode(_param);
  }

  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    else if (! (o instanceof ParamType))
      return false;

    ParamType type = (ParamType) o;

    if (! _type.equals(type._type))
      return false;

    if (_param.length != type._param.length)
      return false;

    for (int i = 0; i < _param.length; i++) {
      if (! _param[i].equals(type._param[i]))
        return false;
    }

    return true;
  }

  @Override
  public String getSimpleName()
  {
    StringBuilder sb = new StringBuilder();

    sb.append(getRawClass().getSimpleName());
    sb.append("<");

    for (int i = 0; i < _param.length; i++) {
      if (i != 0)
        sb.append(",");
      
      sb.append(_param[i].getSimpleName());
    }
    sb.append(">");

    return sb.toString();
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();

    sb.append(getRawClass().toString());
    sb.append("<");

    for (int i = 0; i < _param.length; i++) {
      if (i != 0)
        sb.append(",");
      
      sb.append(_param[i]);
    }
    sb.append(">");

    return sb.toString();
  }
}
