package foo.bar;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StrategyValueObjectProvider implements DefaultValueObjectProvider {
	
	private Map<Class, DefaultValueStrategy<?>> valueStrategiesRegistry = new HashMap<Class, DefaultValueStrategy<?>>();

	// TODO Refactor this fallback into a Decorator pattern
	private DefaultValueObjectProvider defaultValueObjectProvider = new StaticValueObjectProvider();

	public StrategyValueObjectProvider() {
		
		valueStrategiesRegistry.put(String.class, new MethodNameValueStrategy());
	}
	
	public Object getDefaultValueObject(Method method) {
		Class<?> returnType = method.getReturnType();
		if (!valueStrategiesRegistry.containsKey(returnType)) {
			return defaultValueObjectProvider.getDefaultValueObject(method);
		}
		return valueStrategiesRegistry.get(returnType).createValue(method);
	}

	public boolean isDefaultValueRegisteredForType(Class<?> returnType) {
		return valueStrategiesRegistry.containsKey(returnType) || defaultValueObjectProvider.isDefaultValueRegisteredForType(returnType);
	}

}
