package foo.bar;

import net.sf.cglib.proxy.Enhancer;

public class NullObjectProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T wrap(final T original) {
		Enhancer enhancer = new Enhancer();
		Class<T> type = (Class<T>) original.getClass();
		enhancer.setSuperclass(type);
		enhancer.setCallback(new NullObjectMethodInterceptor<T>(original));
		return (T) enhancer.create();
	}

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> type) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setCallback(new NullObjectMethodInterceptor<T>());
		return (T) enhancer.create();
	}


}
