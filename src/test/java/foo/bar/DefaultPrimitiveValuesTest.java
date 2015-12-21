package foo.bar;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Test;

public class DefaultPrimitiveValuesTest {
	
	public static class DefaultValueTypeHolder {
		private char charValue;
		private long longValue;
		private int integerValue;
		private boolean booleanValue;
		private float floatValue;
		private double doubleValue;
		
		public char getCharValue() {
			return charValue;
		}
		public void setCharValue(char stringValue) {
			this.charValue = stringValue;
		}
		public Long getLongValue() {
			return longValue;
		}
		public void setLongValue(Long longValue) {
			this.longValue = longValue;
		}
		public Integer getIntegerValue() {
			return integerValue;
		}
		public void setIntegerValue(Integer integerValue) {
			this.integerValue = integerValue;
		}
		public Boolean getBooleanValue() {
			return booleanValue;
		}
		public void setBooleanValue(Boolean booleanValue) {
			this.booleanValue = booleanValue;
		}
		public float getFloatValue() {
			return floatValue;
		}
		public void setFloatValue(float bigIntegerValue) {
			this.floatValue = bigIntegerValue;
		}
		public double getDoubleValue() {
			return doubleValue;
		}
		public void setDoubleValue(double bigDecimalValue) {
			this.doubleValue = bigDecimalValue;
		}
	}

	@Test
	public void testNullString() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals('\u0000', proxy.getCharValue());
	}

	@Test
	public void testNotNullString() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setCharValue('C');
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals('C', proxy.getCharValue());
	}

	@Test
	public void testNullLong() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new Long(0L), proxy.getLongValue());
	}

	@Test
	public void testNotNullLong() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setLongValue(new Long(0L));
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new Long(0L), proxy.getLongValue());
	}

	@Test
	public void testNullInteger() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new Integer(0), proxy.getIntegerValue());
	}

	@Test
	public void testNotNullInteger() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setIntegerValue(new Integer(99));
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new Integer(99), proxy.getIntegerValue());
	}

	@Test
	public void testNullBoolean() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new Boolean(false), proxy.getBooleanValue());
	}

	@Test
	public void testNotNullBoolean() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setBooleanValue(new Boolean(true));
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new Boolean(true), proxy.getBooleanValue());
	}

	@Test
	public void testNullBigInteger() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(0f, proxy.getFloatValue(), 0f);
	}

	@Test
	public void testNotNullBigInteger() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setFloatValue(0.5f);
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(0.5f, proxy.getFloatValue(), 0f);
	}

	@Test
	public void testNullBigDecimal() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(0d, proxy.getDoubleValue(), 0d);
	}

	@Test
	public void testNotNullBigDecimal() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setDoubleValue(0.5d);
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(0.5d, proxy.getDoubleValue(), 0d);
	}

}
