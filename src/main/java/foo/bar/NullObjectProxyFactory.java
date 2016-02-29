package foo.bar;

import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;

public class NullObjectProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T wrap(final T original) {
		Class<T> type = (Class<T>) original.getClass();
		return create(type, original, null);
	}

	public static <T> T create(Class<T> type) {
		return create(type, null, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T wrap(final T original, DefaultValueObjectProvider provider) {
		Class<T> type = (Class<T>) original.getClass();
		return create(type, original, provider);
	}

	public static <T> T create(Class<T> type, DefaultValueObjectProvider provider) {
		return create(type, null, provider);
	}
	
 	@SuppressWarnings("unchecked")
	private static <T> T create(Class<T> type, final T original, DefaultValueObjectProvider provider) {
		if (Modifier.isFinal(type.getModifiers())) {
			return original;
		}
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setCallback(new NullObjectMethodInterceptor<T>(original, provider));
		return (T) enhancer.create();
	}


}
