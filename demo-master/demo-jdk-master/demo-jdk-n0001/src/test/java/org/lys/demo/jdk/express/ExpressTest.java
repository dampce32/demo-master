package org.lys.demo.jdk.express;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExpressTest {

	public static void test1() throws ScriptException {  
        String str = "(a >= 0 && a <= 5)";  
        ScriptEngineManager manager = new ScriptEngineManager();  
        ScriptEngine engine = manager.getEngineByName("js");  
        engine.put("a", 4);  
        Object result = engine.eval(str);  
        System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);  
    }  
      
    public static void test2() throws ScriptException {  
        String str = "0.8*(2 + 1.4)+2*32/(3-2.1)";  
        ScriptEngineManager manager = new ScriptEngineManager();  
        ScriptEngine engine = manager.getEngineByName("js");  
        Object result = engine.eval(str);  
        System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);  
    }  
      
    public static void main(String[] args) throws ScriptException {  
        test1();  
        test2();  
    }  

}
