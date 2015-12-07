# Motivation

Mappers. That's why.

You know, the kind of code you write once in a while to map the values of properties from a graph of objects into another thing.

```java
public void doSomethingWith(Person person) {
  ...
  String phone = person.getContact().getPhone();
  // Now do something with the phone
}
```

What if `person.getContact()` returns `null`? Exactly, you get a `NullPointerException`.

The most obvious thing to do to prevent from getting `NullPointerException` would be to write some **defensive code** like this:

```java
public void doSomethingWith(Person person) {
  ...
  if (person.getContact() != null) {
    String phone = person.getContact().getPhone();
    // Now do something with the phone
  }
}
```

Simple enough. But what if there are **10ths** of properties to map? How about `a.getB().getC().getD()`? How fun would _that_ be? 

Because, just like me, you _love_ writing defensive code in Java, right? :sarcasm: (oops, no emoji for that)

# Null Object Pattern to the rescue!

I would describe [Null Object Pattern](https://en.wikipedia.org/wiki/Null_Object_pattern#Java) as a technique for providing a **default object** when `null` is encountered.

In the example above, a default (empty) contact whould be set before calling `doSomethingWith(person)` method.

```java
if (person.getContact() == null) {
  Contact emptyContact = new Contact();
  person.setContact(emptyContact);
}

doSomethingWith(person)
```

Now you can write the mapping code like this:

```java
public void doSomethingWith(Person person) {
  // Worries-free, getContact() will not return null
  String phone = person.getContact().getPhone();
  // Now do something with the phone
}
```

But, what good that is? You still have to check if contact is `null` beforehand, except, now that code is separated from the mapping code. Can we take advantage of that? Can we do something to _automagically_ set `null objects` for all properties that apply?

# An implementation of NullObject pattern with Dynamic Proxies

The idea is simple:

> Wrap the target object an intercept all calls to methods, such that if the original call would return null, return a `NullObject` instead.

So, we have to do 2 things:

1. Intercept method calls
2. Create `NullObject` instances of some `Class` known at runtime.
 
This is a perfect job for [Dynamic Proxies](https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html).

I use [Spring](http://spring.io/) on a daily basis, and I know Spring uses [AOP and Dynamic Proxies](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-introduction-proxies) heavily to support annotations like [@Transactional](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-introduction-proxies), but so far I've never needed to implement a Dynamic Proxy myself. Time to get my hands on this subject. Plus, this seems to be the perfect case to get started, doesn't it?

**Disclaimer**: First thing I did was a Google search. I found [this article](http://www.codeproject.com/Articles/33409/Implementing-the-Null-Object-Pattern-with-a-proxy), but I wanted to give it a try myself.

## Dynamic Proxies, the Java way

Let's go back to creating proxies and intercepting method calls. Java provides a way to do this via [java.lang.reflect.Proxy and java.lang.reflect.InvocationHandler](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Proxy.html).

Here's an example of a **nearly-pass-through** proxy that prefixes "Proxy says " to each call to `Foo.sayHello()`:

```java
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
```

Wow, that's a little verbose for something so simple. Two things to take notice here:

1. Java can only make proxies to `Interfaces`. That's why there's an (unnecessary) `FooInterface`, just for the sake of `java.lang.reflect.Proxy`.
2. Standard `java.lang.reflect.InvocationHandler` doesn't take any object to wrap calls to, so I'm taking advantage of **anonymous classes** and pass `final Foo foo` instance to `method.invoke(foo, args)` that's defined in the scope of the `@Test`method.
 
Now that we get a handle of how to make a Dynamic Proxy, let's move on to see how to get rid of the **Interfaces only** restiction, since we need to make proxies to plain old Java beans for our Mapper scenario.

## Code generation: thanks cglib

[cglib](https://github.com/cglib/cglib) is one of those libs you hear a lot about its magic, but that you never have to deal with it directly. **cglib** stands for Code Generation Library. It's the piece of our puzzle that will enable us to create Dynamic Proxies of _plain old Java beans_ that implement no interface at all.

Using a code generation library sounds scary. Fortunately, [cglib's tutorial](https://github.com/cglib/cglib/wiki/Tutorial) is full of straight-forward examples (oh, examples!).

The previous `nearly-pass-through` example would now look like this:

```java
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
```

Look Ma! No interfaces!
