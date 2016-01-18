package foo.bar;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NullObjectProxyTest {
	
	@Test
	public void itCanCreateAProxyToABean() {
		Person person = new Person();
		Person proxy = NullObjectProxyFactory.wrap(person);
		assertNotNull(proxy);
		assertNotEquals(person, proxy);
	}

	@Test
	public void itReturnsEmptyStringIfNull() {
		Person person = new Person();
		person.setName(null);
		Person proxy = NullObjectProxyFactory.wrap(person);
		assertEquals("", proxy.getName());
	}
	
	@Test
	public void itReturnsOriginalStringIfNotNull() {
		Person person = new Person();
		person.setName("Han Solo");
		Person proxy = NullObjectProxyFactory.wrap(person);
		assertEquals("Han Solo", proxy.getName());
	}

	@Test
	public void itReturnsNullObjectProxyWhenBeanHoldsNullValue() {
		Person person = new Person();
		person.setSalary(null);
		Person proxy = NullObjectProxyFactory.wrap(person);
		assertNotNull(proxy.getSalary());
		assertNotNull(proxy.getSalary().getNetSalary());
	}

	@Test
	public void itReturnsOriginalBeanIfNotNullValue() {
		Person person = new Person();
		Salary salary = new Salary();
		person.setSalary(salary);
		salary.setNetSalary(new BigDecimal("1234.56"));
		Person proxy = NullObjectProxyFactory.wrap(person);
		assertEquals(new BigDecimal("1234.56"), proxy.getSalary().getNetSalary());
	}

	public static final class FinalTestClass {
		
	}
	
	/**
	 * Actually this test show the limitations of the current implementation
	 * to wrap a bean that is an instance of a final class; it will just return the bean as is.
	 */
	@Test
	public void itReturnsOriginalBeanIfItIsInstanceOfFinalClass() {
		FinalTestClass bean = new FinalTestClass();
		FinalTestClass proxy = NullObjectProxyFactory.wrap(bean);
		assertTrue(proxy == bean);
	}
}
