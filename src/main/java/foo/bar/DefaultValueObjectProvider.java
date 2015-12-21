package foo.bar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultValueObjectProvider {
	
	private Map<Class, Object> defaultValuesRegistry = new HashMap<Class, Object>();
	
	public DefaultValueObjectProvider() {
		// Primitive Types
		defaultValuesRegistry.put(char.class, '\u0000');
		defaultValuesRegistry.put(long.class, 0L);
		defaultValuesRegistry.put(int.class, 0);
		defaultValuesRegistry.put(boolean.class, false);
		defaultValuesRegistry.put(float.class, 0f);
		defaultValuesRegistry.put(double.class, 0d);
		
		// Some important Classes
		defaultValuesRegistry.put(String.class, "");
		defaultValuesRegistry.put(Long.class, new Long(0L));
		defaultValuesRegistry.put(Integer.class, new Integer(0));
		defaultValuesRegistry.put(Boolean.class, new Boolean(false));
		defaultValuesRegistry.put(Date.class, new Date(0L));
		defaultValuesRegistry.put(BigDecimal.class, new BigDecimal(0));
		defaultValuesRegistry.put(BigInteger.class, new BigInteger("0"));
	}
	
	public Object getDefaultValueObject(Class<?> returnType) {
		return defaultValuesRegistry.get(returnType);
	}

	public boolean isDefaultValueRegisteredForType(Class<?> returnType) {
		return defaultValuesRegistry.containsKey(returnType);
	}

}
