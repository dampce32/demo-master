package org.lys.demo.ehcache.n0001;

import org.junit.Test;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


public class AppTest {

	@Test
	public void testApp() {
		 try {
			System.out.println(System.getProperty("java.io.tmpdir"));
			 
			 final CacheManager manager = CacheManager.create();
			 System.out.println(manager.getCache("D_ID_CACHE"));
			 System.out.println(manager.getCache("AutoAssignUsers"));
			 if(manager.getCache("AutoAssignUsers")==null){
				 manager.addCache("AutoAssignUsers");
			 }
			 //manager.addCacheIfAbsent("D_ID_CACHE");如果D_ID_CACHE在ehcache.xml中没有配置创建,可以通过这个方法进行创建
			 manager.getCache("AutoAssignUsers").put(new Element("name","AutoAssignUsersa"));
			 System.out.println( manager.getCache("AutoAssignUsers"));
			 System.out.println( manager.getCache("AutoAssignUsers").get("name"));
//manager.getCache("AutoAssignUsers").get("bean0").getObjectValue()用这个方法来获取对应的Value,可以强转为对象.
			//manager.shutdown(); ehcache进行关闭
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
