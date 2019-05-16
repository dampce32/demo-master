package org.lys.demo.jdk.jdk8;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

public class JDK8Test {
	/**
	 * Lambda 表达式
	 * 
	 * @createTime: 2019年5月16日 上午11:58:48
	 * @author: lin.yisong
	 */
	@Test
	public void testLambda() {
		/*
		 * https://www.runoob.com/java/java8-lambda-expressions.html Lambda
		 * 表达式，也可称为闭包，它是推动 Java 8 发布的最重要新特性。 Lambda 允许把函数作为一个方法的参数（函数作为参数传递进方法中）。
		 * 
		 * 使用 Lambda 表达式可以使代码变的更加简洁紧凑。
		 * 
		 * 语法 lambda 表达式的语法格式如下：
		 * 
		 * (parameters) -> expression 或 (parameters) ->{ statements; }
		 * 以下是lambda表达式的重要特征:
		 * 
		 * 可选类型声明：不需要声明参数类型，编译器可以统一识别参数值。 可选的参数圆括号：一个参数无需定义圆括号，但多个参数需要定义圆括号。
		 * 可选的大括号：如果主体包含了一个语句，就不需要使用大括号。
		 * 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定明表达式返回了一个数值。
		 */
		JDK8Test tester = new JDK8Test();
		// 类型声明
		MathOperation addition = (int a, int b) -> a + b;
		// 不用类型声明
		MathOperation subtraction = (a, b) -> a - b;
		// 大括号中的返回语句
		MathOperation multiplication = (int a, int b) -> {
			return a * b;
		};
		// 没有大括号及返回语句
		MathOperation division = (int a, int b) -> a / b;

		System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
		System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
		System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
		System.out.println("10 / 5 = " + tester.operate(10, 5, division));

		// 不用括号
		GreetingService greetService1 = message -> System.out.println("Hello " + message);

		// 用括号
		GreetingService greetService2 = (message) -> System.out.println("Hello " + message);

		greetService1.sayMessage("Runoob");
		greetService2.sayMessage("Google");
	}

	/**
	 * 方法引用
	 * 
	 * @createTime: 2019年5月16日 下午1:47:01
	 * @author: lin.yisong
	 */
	@Test
	public void testMethodRef() {
		List<String> names = new ArrayList<String>();
		names.add("Google");
		names.add("Runoob");
		names.add("Taobao");
		names.add("Baidu");
		names.add("Sina");
		names.forEach(System.out::println);
		// 构造器引用：它的语法是Class::new，或者更一般的Class< T >::new实例如下：
		final Car car = Car.create(Car::new);
		final List<Car> cars = Arrays.asList(car);
		// 静态方法引用：它的语法是Class::static_method，实例如下：
		cars.forEach(Car::collide);
		// 特定类的任意对象的方法引用：它的语法是Class::method实例如下：
		cars.forEach(Car::repair);
		// 特定对象的方法引用：它的语法是instance::method实例如下：
		final Car police = Car.create(Car::new);
		cars.forEach(police::follow);
	}

	@Test
	public void testFunctionalInterface() {
		/*
		 * 函数式接口(Functional Interface)就是一个有且仅有一个抽象方法，但是可以有多个非抽象方法的接口。 函数式接口可以被隐式转换为
		 * lambda 表达式。 Lambda 表达式和方法引用（实际上也可认为是Lambda表达式）上。
		 */
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

		// Predicate<Integer> predicate = n -> true
		// n 是一个参数传递到 Predicate 接口的 test 方法
		// n 如果存在则 test 方法返回 true

		System.out.println("输出所有数据:");
		// 传递参数 n
		eval(list, n -> true);

		// Predicate<Integer> predicate1 = n -> n%2 == 0
		// n 是一个参数传递到 Predicate 接口的 test 方法
		// 如果 n%2 为 0 test 方法返回 true

		System.out.println("输出所有偶数:");
		eval(list, n -> n % 2 == 0);

		// Predicate<Integer> predicate2 = n -> n > 3
		// n 是一个参数传递到 Predicate 接口的 test 方法
		// 如果 n 大于 3 test 方法返回 true

		System.out.println("输出大于 3 的所有数字:");
		eval(list, n -> n > 3);
	}

	@Test
	public void testDefaultMethods() {
		Vehicle vehicle = new Car2();
		vehicle.print();
	}

	@Test
	public void testStreams() {
		System.out.println("使用 Java 7: ");

		// 计算空字符串
		List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
		System.out.println("列表: " + strings);
		long count = getCountEmptyStringUsingJava7(strings);

		System.out.println("空字符数量为: " + count);
		count = getCountLength3UsingJava7(strings);

		System.out.println("字符串长度为 3 的数量为: " + count);

		// 删除空字符串
		List<String> filtered = deleteEmptyStringsUsingJava7(strings);
		System.out.println("筛选后的列表: " + filtered);

		// 删除空字符串，并使用逗号把它们合并起来
		String mergedString = getMergedStringUsingJava7(strings, ", ");
		System.out.println("合并字符串: " + mergedString);
		List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

		// 获取列表元素平方数
		List<Integer> squaresList = getSquares(numbers);
		System.out.println("平方数列表: " + squaresList);
		List<Integer> integers = Arrays.asList(1, 2, 13, 4, 15, 6, 17, 8, 19);

		System.out.println("列表: " + integers);
		System.out.println("列表中最大的数 : " + getMax(integers));
		System.out.println("列表中最小的数 : " + getMin(integers));
		System.out.println("所有数之和 : " + getSum(integers));
		System.out.println("平均数 : " + getAverage(integers));
		System.out.println("随机数: ");

		// 输出10个随机数
		Random random = new Random();

		for (int i = 0; i < 10; i++) {
			System.out.println(random.nextInt());
		}

		System.out.println("使用 Java 8: ");
		System.out.println("列表: " + strings);

		count = strings.stream().filter(string -> string.isEmpty()).count();
		System.out.println("空字符串数量为: " + count);

		count = strings.stream().filter(string -> string.length() == 3).count();
		System.out.println("字符串长度为 3 的数量为: " + count);

		filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
		System.out.println("筛选后的列表: " + filtered);

		mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
		System.out.println("合并字符串: " + mergedString);

		squaresList = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
		System.out.println("Squares List: " + squaresList);
		System.out.println("列表: " + integers);

		IntSummaryStatistics stats = integers.stream().mapToInt((x) -> x).summaryStatistics();

		System.out.println("列表中最大的数 : " + stats.getMax());
		System.out.println("列表中最小的数 : " + stats.getMin());
		System.out.println("所有数之和 : " + stats.getSum());
		System.out.println("平均数 : " + stats.getAverage());
		System.out.println("随机数: ");

		random.ints().limit(10).sorted().forEach(System.out::println);

		// 并行处理
		count = strings.parallelStream().filter(string -> string.isEmpty()).count();
		System.out.println("空字符串的数量为: " + count);
	}

	@Test
	public void testOptionalClass() {
		Integer value1 = null;
		Integer value2 = new Integer(10);

		// Optional.ofNullable - 允许传递为 null 参数
		Optional<Integer> a = Optional.ofNullable(value1);

		// Optional.of - 如果传递的参数是 null，抛出异常 NullPointerException
		Optional<Integer> b = Optional.of(value2);
		System.out.println(sum(a, b));
	}

	@Test
	public void testNashornJavascript() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");

		String name = "Runoob";
		Integer result = null;

		try {
			nashorn.eval("print('" + name + "')");
			result = (Integer) nashorn.eval("10 + 2");

		} catch (ScriptException e) {
			System.out.println("执行脚本错误: " + e.getMessage());
		}

		System.out.println(result.toString());
	}

	@Test
	public void testDatetimeApi() {
		// 获取当前的日期时间
		LocalDateTime currentTime = LocalDateTime.now();
		System.out.println("当前时间: " + currentTime);

		LocalDate date1 = currentTime.toLocalDate();
		System.out.println("date1: " + date1);

		Month month = currentTime.getMonth();
		int day = currentTime.getDayOfMonth();
		int seconds = currentTime.getSecond();

		System.out.println("月: " + month + ", 日: " + day + ", 秒: " + seconds);

		LocalDateTime date2 = currentTime.withDayOfMonth(10).withYear(2012);
		System.out.println("date2: " + date2);

		// 12 december 2014
		LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
		System.out.println("date3: " + date3);

		// 22 小时 15 分钟
		LocalTime date4 = LocalTime.of(22, 15);
		System.out.println("date4: " + date4);

		// 解析字符串
		LocalTime date5 = LocalTime.parse("20:15:30");
		System.out.println("date5: " + date5);

		// 获取当前时间日期
		ZonedDateTime date6 = ZonedDateTime.parse("2015-12-03T10:15:30+05:30[Asia/Shanghai]");
		System.out.println("date6: " + date6);

		ZoneId id = ZoneId.of("Europe/Paris");
		System.out.println("ZoneId: " + id);

		ZoneId currentZone = ZoneId.systemDefault();
		System.out.println("当期时区: " + currentZone);
	}

	@Test
	public void testBase64() {
		try {
			// 使用基本编码
			String base64encodedString = Base64.getEncoder().encodeToString("runoob?java8".getBytes("utf-8"));
			System.out.println("Base64 编码字符串 (基本) :" + base64encodedString);

			// 解码
			byte[] base64decodedBytes = Base64.getDecoder().decode(base64encodedString);

			System.out.println("原始字符串: " + new String(base64decodedBytes, "utf-8"));
			base64encodedString = Base64.getUrlEncoder().encodeToString("TutorialsPoint?java8".getBytes("utf-8"));
			System.out.println("Base64 编码字符串 (URL) :" + base64encodedString);

			StringBuilder stringBuilder = new StringBuilder();

			for (int i = 0; i < 10; ++i) {
				stringBuilder.append(UUID.randomUUID().toString());
			}

			byte[] mimeBytes = stringBuilder.toString().getBytes("utf-8");
			String mimeEncodedString = Base64.getMimeEncoder().encodeToString(mimeBytes);
			System.out.println("Base64 编码字符串 (MIME) :" + mimeEncodedString);

		} catch (UnsupportedEncodingException e) {
			System.out.println("Error :" + e.getMessage());
		}
	}

	public Integer sum(Optional<Integer> a, Optional<Integer> b) {

		// Optional.isPresent - 判断值是否存在

		System.out.println("第一个参数值存在: " + a.isPresent());
		System.out.println("第二个参数值存在: " + b.isPresent());

		// Optional.orElse - 如果值存在，返回它，否则返回默认值
		Integer value1 = a.orElse(new Integer(0));

		// Optional.get - 获取值，值需要存在
		Integer value2 = b.get();
		return value1 + value2;
	}

	private static int getCountEmptyStringUsingJava7(List<String> strings) {
		int count = 0;
		for (String string : strings) {
			if (string.isEmpty()) {
				count++;
			}
		}
		return count;
	}

	private static int getCountLength3UsingJava7(List<String> strings) {
		int count = 0;
		for (String string : strings) {
			if (string.length() == 3) {
				count++;
			}
		}
		return count;
	}

	private static List<String> deleteEmptyStringsUsingJava7(List<String> strings) {
		List<String> filteredList = new ArrayList<String>();
		for (String string : strings) {
			if (!string.isEmpty()) {
				filteredList.add(string);
			}
		}
		return filteredList;
	}

	private static String getMergedStringUsingJava7(List<String> strings, String separator) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : strings) {
			if (!string.isEmpty()) {
				stringBuilder.append(string);
				stringBuilder.append(separator);
			}
		}
		String mergedString = stringBuilder.toString();
		return mergedString.substring(0, mergedString.length() - 2);
	}

	private static List<Integer> getSquares(List<Integer> numbers) {
		List<Integer> squaresList = new ArrayList<Integer>();
		for (Integer number : numbers) {
			Integer square = new Integer(number.intValue() * number.intValue());
			if (!squaresList.contains(square)) {
				squaresList.add(square);
			}
		}
		return squaresList;
	}

	private static int getMax(List<Integer> numbers) {
		int max = numbers.get(0);
		for (int i = 1; i < numbers.size(); i++) {
			Integer number = numbers.get(i);
			if (number.intValue() > max) {
				max = number.intValue();
			}
		}
		return max;
	}

	private static int getMin(List<Integer> numbers) {
		int min = numbers.get(0);
		for (int i = 1; i < numbers.size(); i++) {
			Integer number = numbers.get(i);
			if (number.intValue() < min) {
				min = number.intValue();
			}
		}
		return min;
	}

	@SuppressWarnings("rawtypes")
	private static int getSum(List numbers) {
		int sum = (int) (numbers.get(0));
		for (int i = 1; i < numbers.size(); i++) {
			sum += (int) numbers.get(i);
		}
		return sum;
	}

	private static int getAverage(List<Integer> numbers) {
		return getSum(numbers) / numbers.size();
	}

	interface Vehicle {
		default void print() {
			System.out.println("我是一辆车!");
		}

		static void blowHorn() {
			System.out.println("按喇叭!!!");
		}
	}

	interface FourWheeler {
		default void print() {
			System.out.println("我是一辆四轮车!");
		}
	}

	class Car2 implements Vehicle, FourWheeler {
		public void print() {
			Vehicle.super.print();
			FourWheeler.super.print();
			Vehicle.blowHorn();
			System.out.println("我是一辆汽车!");
		}
	}

	public void eval(List<Integer> list, Predicate<Integer> predicate) {
		for (Integer n : list) {
			if (predicate.test(n)) {
				System.out.println(n + " ");
			}
		}
	}

	interface MathOperation {
		int operation(int a, int b);
	}

	interface GreetingService {
		void sayMessage(String message);
	}

	private int operate(int a, int b, MathOperation mathOperation) {
		return mathOperation.operation(a, b);
	}
}
