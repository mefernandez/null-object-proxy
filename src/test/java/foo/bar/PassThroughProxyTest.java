package foo.bar;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;

public class PassThroughProxyTest {
	
	public interface FooInterface {

		String sayHello();

	}
	
	public static class Foo implements FooInterface {
		
		public String sayHello() {
			return "Hello";
		}
		
	}

	@Test
	public void test() {
		final Foo foo = new Foo();
		FooInterface proxy = (FooInterface) Proxy.newProxyInstance(Foo.class.getClassLoader(), new Class<?>[] { FooInterface.class }, new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return "Proxy says " + method.invoke(foo, args);
			}
			
		});
		
		assertEquals("Proxy says Hello", proxy.sayHello());
	}

}
