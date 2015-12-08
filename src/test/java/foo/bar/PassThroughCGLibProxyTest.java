package foo.bar;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

public class PassThroughCGLibProxyTest {
	
	public static class Foo {
		
		public String sayHello() {
			return "Hello";
		}
		
	}

	@Test
	public void test() {
		final Foo foo = new Foo();
		
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Foo.class);
		enhancer.setCallback(new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return "Proxy says " + method.invoke(foo, args);
			}	
		});
		
		Foo proxy = (Foo) enhancer.create();
		
		assertEquals("Proxy says Hello", proxy.sayHello());
	}

}
