package org.sglj.util.test;

import junit.framework.Assert;

import org.junit.Test;
import org.sglj.util.TypeConversionUtils;

public class TypeConversionTest {

	@Test
	public void testInt() {
		Assert.assertEquals(Integer.valueOf(123),
				TypeConversionUtils.stringToInteger("123"));
		Assert.assertNull(TypeConversionUtils.stringToInteger("123.0"));
		Assert.assertNull(TypeConversionUtils.stringToInteger(null));
	}
	
	@Test
	public void testShort() {
		Assert.assertEquals(Short.valueOf((short) 234), 
				TypeConversionUtils.stringToShort("234"));
		Assert.assertNull(TypeConversionUtils.stringToShort("1234566"));
		Assert.assertNull(TypeConversionUtils.stringToShort(null));
	}
	
	@Test
	public void testByte() {
		Assert.assertEquals(Byte.valueOf((byte) 23),
				TypeConversionUtils.stringToByte("23"));
		Assert.assertNull(TypeConversionUtils.stringToByte("1234"));
		Assert.assertNull(TypeConversionUtils.stringToByte(null));
	}
	
	@Test
	public void testCharacter() {
		Assert.assertSame(Character.valueOf('a'),
				TypeConversionUtils.stringToCharacter("a"));
		Assert.assertNull(TypeConversionUtils.stringToCharacter(""));
		Assert.assertNull(TypeConversionUtils.stringToCharacter("aa"));
		Assert.assertNull(TypeConversionUtils.stringToCharacter(null));
	}
	
	@Test
	public void testFloat() {
		Assert.assertEquals(23.45f,
				TypeConversionUtils.stringToFloat("23.45f"));
		Assert.assertNull(TypeConversionUtils.stringToFloat("--0.23"));
		Assert.assertNull(TypeConversionUtils.stringToFloat(null));
	}
	
	@Test
	public void testGenericConversion0() {
		Integer i = TypeConversionUtils.stringToBasicType("23", Integer.class);
		Assert.assertTrue(i.intValue() == 23);
		
		Character c = TypeConversionUtils.stringToBasicType("a", 
				Character.class);
		Assert.assertTrue(c.charValue() == 'a');
	}
	
	@Test
	public void testGenericConversion2() {
		Integer someObject = new Integer(888);
		
		@SuppressWarnings("unchecked")
		Class<Integer> clazz = (Class<Integer>) someObject.getClass();
		
		Integer converted = TypeConversionUtils.toBasicType("23", clazz);
		Assert.assertTrue(23 == ((Integer) converted).intValue());
	}
	
	@Test
	public void testGenericConversion1() {
		Object someObject = new Integer(888);
		
		Object converted = TypeConversionUtils.toBasicType("23", 
				someObject.getClass());
		if (!(converted instanceof Integer)) {
			Assert.fail();
			return ;
		}
		Assert.assertTrue(23 == ((Integer) converted).intValue());
	}
	
}
