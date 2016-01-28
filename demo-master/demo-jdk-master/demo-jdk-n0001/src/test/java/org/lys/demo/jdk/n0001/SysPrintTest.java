package org.lys.demo.jdk.n0001;

import org.junit.Test;

public class SysPrintTest {

	@Test
	public void test() {
		String name = "me";
		int age = 18;
		String sql ="My name is "+name+"， I am "+age+" years old.";
		System.out.println(sql);
		System.out.printf("My name is %S， I am %d years old.", name, age);
	}

}
