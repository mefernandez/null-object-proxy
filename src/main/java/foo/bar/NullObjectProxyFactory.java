package foo.bar;

import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;

public class NullObjectProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T wrap(final T original) {
		Class<T> type = (Class<T>) original.getClass();
		return create(type, original);
	}

	public static <T> T create(Class<T> type) {
		return create(type, null);
	}

	@SuppressWarnings("unchecked")
	private static <T> T create(Class<T> type, final T original) {
		if (Modifier.isFinal(type.getModifiers())) {
			return original;
		}
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setCallback(new NullObjectMethodInterceptor<T>(original));
		return (T) enhancer.create();
	}


}
