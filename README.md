[ ![Codeship Status for mefernandez/null-object-proxy](https://codeship.com/projects/0f4b25b0-813d-0133-281b-6eebf45a905d/status?branch=master)](https://codeship.com/projects/121170)

## Table of Contents:
- [Motivation](#motivation)
- [Null Object pattern to the rescue!](#null-object-pattern-to-the-rescue)
- [An implementation of Null Object pattern with Dynamic Proxies](#an-implementation-of-nullobject-pattern-with-dynamic-proxies)
- [An algorithm to wrap a graph of beans with Null Object](#an-algorithm-to-wrap-a-graph-of-beans-with-nullobject)
- [How to use this on your project](#how-to-use-this-on-your-project)

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

> Wrap the target object and intercept all calls to methods, such that if the original call would return null, return a `NullObject` instead.

So, we have to do 2 things:

1. Intercept method calls
2. Create `NullObject` instances of some `Class` known at runtime.
 
This is a perfect job for [Dynamic Proxies](https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html).

I use [Spring](http://spring.io/) on a daily basis, and I know Spring uses [AOP and Dynamic Proxies](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-introduction-proxies) heavily to support annotations like [@Transactional](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#aop-introduction-proxies), but so far I've never needed to implement a Dynamic Proxy myself. Time to get my hands on this subject. Plus, this seems to be the perfect case to get started, doesn't it?

### Preliminary searches for the state of the art

First thing I did was a Google search. I found [this article](http://www.codeproject.com/Articles/33409/Implementing-the-Null-Object-Pattern-with-a-proxy), but I wanted to give it a try myself.

**Edited**: A few days after writing this I came across [this article from RebelLabs](http://zeroturnaround.com/rebellabs/java-8-best-practices-cheat-sheet/) about [Java 8's `java.util.Optional`](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) I was not aware of.
Although `java.util.Optional` aims at dealing with `null` references, it seems you need to use `lambdas` to access objects instead of just calling good ol' `getters`, so I still think this humble approach via Dynamic Proxies is still relevant. 

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

So, now we have the essential tools to create **Dynamic Proxies** and **intercept method calls**. Next, we'll put these to work creating `NullObject` instances when appropriate.

# An algorithm to wrap a graph of beans with NullObject

It all starts with a plain old Java object, a _POJO_, or a Bean if you will. This Bean declares relationships with other Beans, which can be retrieved via getters. A picture worths a thousand words:

![A tree of Beans](http://yuml.me/03404357)

That's a _tree_ of related Beans. I know I mentioned a _graph_ earlier, but let's stick to a tree for now. Because XMas.

Notice I've introduced a `java.lang.String` property in **Bean D**, because that's what you utterly need to work with in a Mapper: the value of a [`String`, `Date`, `int`, `boolean`, etc.].

So, to get **String name** in **Bean D** starting from **Bean A** you would need to write a code like this:

```java
String name = a.getB().getD().getName();
// Do something with the name
```

We need to **design an algorithm** that prevents `NullPointerException` from raising if `null` is encountered in that chain of method calls.

Let's try this algorithm:

1. Wrap the starting bean with a Dynamic Proxy to intercept method calls.
2. If a call to a method returns `null`:
  1. If the return type is one of [`String`, `Date`, etc.], then return a **default value** of that type.
  2. Else, return a `NullObject` for that type.
3. If a call to a method returns **not** `null`:
  1. If the return type is one of [`String`, `Date`, etc.], then return **as is**.
  2. Else, wrap it as in step 1 and return it.

Piece of cake. **Default values** are convenient values of well-known Java types. For instance, we could pick:

- `""` for `String`
- `new Date(0L)` for `Date`
- etc.

Note that for **primitive types** there's no need to take care, since primitives define their own default values.

The algorithm looks simple, but there are some caveats:

- Trying to create `NullObject` for classes that **do not define an empty constructor** won't work out of the box.
- Trying to create `NullObject` for Collections is kinda pesky.
- Default values need to be added on the basis of what types you want to support.
- As said before, the scenario is a tree of Bean dependencies (no closed loops). Watch out for graphs.

# How to use this on your project

The "API" is dead simple:

```java
MyBean proxy = NullObjectProxyFactory.wrap(someBean);
// There. Go wild on that proxy. No NullPointerExceptions ahead.
```

To include this lib in your **Maven** project, add this to your `pom.xml`:

```maven
<dependency>
	<groupId>com.github.mefernandez</groupId>
	<artifactId>null-object-proxy</artifactId>
	<version>0.0.4</version>
</dependency>
```

Thanks [JitPack](https://jitpack.io/)!


**Disclaimer**: At the time of writing (December 2015), this project is in early stages, thus not fully ready for production. **So, beware!**

# Comments welcome

Oh yes please! Open an issue if it's code-related or just mention [@eloy_iv](https://twitter.com/eloy_iv) at [Twitter](https://twitter.com)
