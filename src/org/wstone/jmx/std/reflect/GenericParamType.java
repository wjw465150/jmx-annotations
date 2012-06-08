package org.wstone.jmx.std.reflect;

import java.util.HashMap;


/**
 * param type matching
 */
public class GenericParamType extends ParamType
{
  public GenericParamType(Class<?> type,
                          BaseType []param,
                          HashMap<String,BaseType> paramMap)
  {
    super(type, param, paramMap);
  }
  
  @Override
  public boolean isGenericRaw()
  {
    return true;
  }
  
  @Override
  public boolean isGeneric()
  {
    // ioc/07f2
    
    return false;
  }
}
