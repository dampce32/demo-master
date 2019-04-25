package org.lys.demo.jdk.util;

import java.text.ParseException;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void dateToStampTest() throws ParseException{
		System.out.println(DateUtil.dateToStamp("08:30", "HH:mm"));;
	}
	
	@Test
	public void stampToDateTest() throws ParseException{
		System.out.println(DateUtil.stampToDate("946627201000", "HH:mm"));;
	}
	
}
