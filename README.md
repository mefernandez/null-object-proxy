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

Simple enough. But what if there are 10ths of properties to map? How about `a.getB().getC().getD()`? How fun would that be? 
Because, just like me, you _love_ writing defensive code in Java, right? :sarcasm: (oops, no emoji for that)

# Null Object Pattern to the rescue!

I would describe [Null Object Pattern](https://en.wikipedia.org/wiki/Null_Object_pattern#Java) as a techinque for providing a **default object** when `null` is encountered.

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

But, what good that is? You still have to check if contact is `null` beforehand, except, now that code is separated from the mapping code. Can we take advantage of that? Can we do something to automagically set `null objects` for all properties that apply?

# An implementation of NullObject pattern with Dynamic Proxies

