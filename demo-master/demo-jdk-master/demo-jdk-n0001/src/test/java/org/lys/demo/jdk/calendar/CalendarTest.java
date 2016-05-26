package org.lys.demo.jdk.calendar;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class CalendarTest {

	@Test
	public void testActualMinimum() {
		Calendar now = Calendar.getInstance();
		System.out.println(now.getActualMinimum(11));//最小是时0小时
		System.out.println(now.getActualMinimum(12));//最小是时0小时
		System.out.println(now.getActualMinimum(13));//最小是时0小时
		fail("Not yet implemented");
	}
	
	@Test
	public void testField() {
		//时0~23
		assertEquals(Calendar.HOUR_OF_DAY, 11);
		//分
		assertEquals(Calendar.MINUTE, 12);
		//秒
		assertEquals(Calendar.SECOND, 13);
		
		
		Calendar now = Calendar.getInstance();
		
		System.out.println(now.get(11));
	}
	

}
