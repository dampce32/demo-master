package top.linyisong.demo.flowable.n0001;

import java.util.List;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.entitylink.api.EntityLink;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

public class FlowableTest2 {
	ProcessEngine processEngine;
	@Before
	public void createProcessEngine() {
		 ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
			      .setJdbcUrl("jdbc:mysql://58.53.39.118:23232/fzlx-dev?useUnicode=true&characterEncoding=utf-8")
			      .setJdbcUsername("fzlx")
			      .setJdbcPassword("fzlx2019!@#")
			      .setJdbcDriver("com.mysql.jdbc.Driver")
			      .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);//生成数据库表
		 processEngine = cfg.buildProcessEngine();
	}
	
	@Test
	public void testQueryHistory() {
		TaskService taskService = processEngine.getTaskService();
		List<Task> list = taskService.createTaskQuery().active().list();
		for (Task task : list) {
			String taskId = task.getId();
			String taskName = task.getName();
			System.out.println(taskId);
			System.out.println(taskName);
		}
		
	}
	
	//37542
	
	@Test
	public void testHistory() {
		String taskId ="40011";
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		//runtimeService.getIdentityLinksForProcessInstance(instanceId)
		List<EntityLink> list = runtimeService.getEntityLinkChildrenForTask(taskId);
		System.out.println(list.size());
	}
	
}
