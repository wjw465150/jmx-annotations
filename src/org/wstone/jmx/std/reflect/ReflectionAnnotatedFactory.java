package org.wstone.jmx.std.reflect;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.WeakHashMap;

/**
 * Factory for introspecting reflected types.
 */
public class ReflectionAnnotatedFactory
{
  private WeakHashMap<Type,SoftReference<ReflectionAnnotatedType<?>>> _typeMap
    = new WeakHashMap<Type,SoftReference<ReflectionAnnotatedType<?>>>();

  private static final ReflectionAnnotatedFactory _factory = new ReflectionAnnotatedFactory();
  
  /**
   * Returns the _factory for the given loader.
   */
  private static ReflectionAnnotatedFactory getCurrent()
  {
    return _factory;
  }

  /**
   * Introspects a simple reflection type, i.e. a type without
   * fields and methods.
   */
  public static <X> ReflectionAnnotatedType<X> introspectType(Class<X> cl)
  {
    return getCurrent().introspectTypeImpl(cl);
  }

  /**
   * Introspects a simple reflection type, i.e. a type without
   * fields and methods.
   */
  public static <X> ReflectionAnnotatedType<X> introspectType(BaseType type)
  {
    return getCurrent().introspectTypeImpl(type);
  }

  /**
   * Introspects the reflection type
   */
  synchronized
  private <X> ReflectionAnnotatedType<X> introspectTypeImpl(Type type)
  {
    SoftReference<ReflectionAnnotatedType<?>> typeRef  = _typeMap.get(type);

    ReflectionAnnotatedType<?> annType = null;

    if (typeRef != null)
      annType = typeRef.get();

    if (annType == null) {
      BaseTypeFactory inject = BaseTypeFactory.getCurrent();
      
      BaseType baseType = inject.createForSource(type);
      
      annType = new ReflectionAnnotatedType<X>(inject, baseType);

      typeRef = new SoftReference<ReflectionAnnotatedType<?>>(annType);

      _typeMap.put(type, typeRef);
    }

    return (ReflectionAnnotatedType<X>) annType;
  }

  /**
   * Introspects the reflection type
   */
  synchronized
  private <X> ReflectionAnnotatedType<X> introspectTypeImpl(BaseType baseType)
  {
    Type type = baseType.toType();
    
    SoftReference<ReflectionAnnotatedType<?>> typeRef
      = _typeMap.get(type);

    ReflectionAnnotatedType<?> annType = null;

    if (typeRef != null)
      annType = typeRef.get();

    if (annType == null) {
      BaseTypeFactory inject = BaseTypeFactory.getCurrent();
      
      annType = new ReflectionAnnotatedType<X>(inject, baseType);

      typeRef = new SoftReference<ReflectionAnnotatedType<?>>(annType);

      _typeMap.put(type, typeRef);
    }

    return (ReflectionAnnotatedType<X>) annType;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[]";
  }
}
