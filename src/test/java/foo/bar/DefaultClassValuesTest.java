package foo.bar;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Test;

public class DefaultClassValuesTest {
	
	public static class DefaultValueTypeHolder {
		private String stringValue;
		private Long longValue;
		private Integer integerValue;
		private Boolean booleanValue;
		private Date dateValue;
		private BigInteger bigIntegerValue;
		private BigDecimal bigDecimalValue;
		
		public String getStringValue() {
			return stringValue;
		}
		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
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
		public Date getDateValue() {
			return dateValue;
		}
		public void setDateValue(Date dateValue) {
			this.dateValue = dateValue;
		}
		public BigInteger getBigIntegerValue() {
			return bigIntegerValue;
		}
		public void setBigIntegerValue(BigInteger bigIntegerValue) {
			this.bigIntegerValue = bigIntegerValue;
		}
		public BigDecimal getBigDecimalValue() {
			return bigDecimalValue;
		}
		public void setBigDecimalValue(BigDecimal bigDecimalValue) {
			this.bigDecimalValue = bigDecimalValue;
		}
	}

	@Test
	public void testNullString() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals("", proxy.getStringValue());
	}

	@Test
	public void testNotNullString() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setStringValue("String");
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals("String", proxy.getStringValue());
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
	public void testNullDate() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new Date(0L), proxy.getDateValue());
	}

	@Test
	public void testNotNullDate() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setDateValue(new Date(10203040L));
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new Date(10203040L), proxy.getDateValue());
	}

	@Test
	public void testNullBigInteger() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new BigInteger("0"), proxy.getBigIntegerValue());
	}

	@Test
	public void testNotNullBigInteger() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setBigIntegerValue(new BigInteger("99"));
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new BigInteger("99"), proxy.getBigIntegerValue());
	}

	@Test
	public void testNullBigDecimal() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new BigDecimal(0L), proxy.getBigDecimalValue());
	}

	@Test
	public void testNotNullBigDecimal() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setBigDecimalValue(new BigDecimal(99L));
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals(new BigDecimal(99L), proxy.getBigDecimalValue());
	}

}
