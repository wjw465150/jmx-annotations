package org.wstone.jmx.std.reflect;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.wstone.jmx.std.util.LruCache;

/**
 * type matching the web bean
 */
public class BaseTypeFactory
{
  private LruCache<Type,BaseType> _sourceCache
    = new LruCache<Type,BaseType>(128);
  
  private LruCache<Type,BaseType> _targetCache
    = new LruCache<Type,BaseType>(128);

  private static final BaseTypeFactory _current = new BaseTypeFactory();
  public static BaseTypeFactory getCurrent() {
    return _current;
  }
  
  public BaseType createForSource(Type type)
  {
    if (type instanceof BaseType)
      return (BaseType) type;
    
    BaseType baseType = _sourceCache.get(type);

    if (baseType == null) {
      baseType = BaseType.createForSource(type,
                                          new HashMap<String,BaseType>(),
                                          null);

      if (baseType == null)
        throw new NullPointerException("unsupported BaseType: " + type + " " + type.getClass());

      _sourceCache.put(type, baseType);
    }

    return baseType;
  }

  public BaseType createForTarget(Type type)
  {
    if (type instanceof BaseType)
      return (BaseType) type;
    
    BaseType baseType = _targetCache.get(type);

    if (baseType == null) {
      baseType = BaseType.createForTarget(type,
                                          new HashMap<String,BaseType>(),
                                          null);

      if (baseType == null)
        throw new NullPointerException("unsupported BaseType: " + type + " " + type.getClass());

      _targetCache.put(type, baseType);
    }

    return baseType;
  }
}
