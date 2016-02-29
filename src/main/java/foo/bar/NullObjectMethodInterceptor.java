package foo.bar;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class NullObjectMethodInterceptor<T> implements MethodInterceptor {
	
	DefaultValueObjectProvider defaultValueObjectProvider;

	private final T original;

	public NullObjectMethodInterceptor() {
		this(null);
	}

	public NullObjectMethodInterceptor(T original) {
		this(original, null);
	}

	public NullObjectMethodInterceptor(T original, DefaultValueObjectProvider defaultValueObjectProvider) {
		this.original = original;
		this.defaultValueObjectProvider = defaultValueObjectProvider != null ? defaultValueObjectProvider : new StaticValueObjectProvider();
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
	    throws Throwable {
		
		Object originalValue = null;
		if (original == null) {
			originalValue = proxy.invokeSuper(obj, args);
		} else {
			originalValue = proxy.invoke(original, args);
		}
		
		Class<?> returnType = method.getReturnType();
		
		if (originalValue == null) {
			Object defaultValue = defaultValueObjectProvider.getDefaultValueObject(method);
			if (defaultValue != null) {
				return defaultValue;
			}
			return NullObjectProxyFactory.create(returnType);
		}
		
		if (defaultValueObjectProvider.isDefaultValueRegisteredForType(returnType)) {
			return originalValue;
		}
		
		
		return NullObjectProxyFactory.wrap(originalValue);
	}

	public DefaultValueObjectProvider getDefaultValueObjectProvider() {
		return defaultValueObjectProvider;
	}

	public void setDefaultValueObjectProvider(DefaultValueObjectProvider defaultValueObjectProvider) {
		this.defaultValueObjectProvider = defaultValueObjectProvider;
	}


}