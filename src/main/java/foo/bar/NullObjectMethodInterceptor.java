package foo.bar;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class NullObjectMethodInterceptor<T> implements MethodInterceptor {
	private final T original;

	public NullObjectMethodInterceptor(T original) {
		this.original = original;
	}

	public NullObjectMethodInterceptor() {
		this(null);
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
			if (returnType == String.class) {
				return "";
			} else if (returnType == BigDecimal.class) {
				// TODO Think of something to create proxies for objects without default constructors
				return new BigDecimal(0);
			}
			return NullObjectProxyFactory.create(returnType);
		}
		
		if (returnType == String.class) {
			return originalValue;
		} else if (returnType == BigDecimal.class) {
			return originalValue;
		}
		
		return NullObjectProxyFactory.wrap(originalValue);
	}
}