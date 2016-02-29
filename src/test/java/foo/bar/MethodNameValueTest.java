package foo.bar;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Test;

public class MethodNameValueTest {
	
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
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder, new StrategyValueObjectProvider());
		assertEquals("getStringValue", proxy.getStringValue());
	}

	@Test
	public void testNotNullString() {
		DefaultValueTypeHolder holder = new DefaultValueTypeHolder();
		holder.setStringValue("String");
		DefaultValueTypeHolder proxy = NullObjectProxyFactory.wrap(holder);
		assertEquals("String", proxy.getStringValue());
	}


}
