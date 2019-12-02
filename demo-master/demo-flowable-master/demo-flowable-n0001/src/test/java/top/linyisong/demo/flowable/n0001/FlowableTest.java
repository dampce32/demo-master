package top.linyisong.demo.flowable.n0001;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

public class FlowableTest {
	ProcessEngine processEngine;
	/**
	 * 创建流程引擎
	 * @createTime: 2019年9月12日 下午3:22:30
	 * @author: lin.yisong
	 */
	@Test
	public void testProcessEngine() {
		 ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
			      .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable-demo?useUnicode=true&characterEncoding=utf-8")
			      .setJdbcUsername("root")
			      .setJdbcPassword("a123456")
			      .setJdbcDriver("com.mysql.jdbc.Driver")
			      .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);//生成数据库表

	    ProcessEngine processEngine = cfg.buildProcessEngine();
	}
	
	@Before
	public void createProcessEngine() {
		 ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
			      .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable-demo?useUnicode=true&characterEncoding=utf-8")
			      .setJdbcUsername("root")
			      .setJdbcPassword("a123456")
			      .setJdbcDriver("com.mysql.jdbc.Driver")
			      .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);//生成数据库表
		 processEngine = cfg.buildProcessEngine();
	}
	
	
	/**
	 * 部署流程定义
	 * @createTime: 2019年9月12日 下午3:28:41
	 * @author: lin.yisong
	 */
	@Test
	public void testDeploy() {
		//部署流程定义
		RepositoryService repositoryService = processEngine.getRepositoryService();
		Deployment deployment = repositoryService.createDeployment()
		  .addClasspathResource("holiday-request.bpmn20.xml")
		  .deploy();
		
		//查询流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				  .deploymentId(deployment.getId())
				  .singleResult();
		
				System.out.println("Found process definition : " + processDefinition.getName());
				
	   //涉及到的表：act_re_procdef（流程定义） act_re_deployment（流程部署）act_ge_bytearray（全局图片）：存放流程图
	}
	/**
	 * 启动流程实例
	 * @createTime: 2019年9月12日 下午3:36:32
	 * @author: lin.yisong
	 */
	@Test
	public void testStartProcessInstance() {
		Scanner scanner= new Scanner(System.in);

		System.out.println("Who are you?");
		String employee = scanner.nextLine();

		System.out.println("How many holidays do you want to request?");
		Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

		System.out.println("Why do you need them?");
		String description = scanner.nextLine();
		
		RuntimeService runtimeService = processEngine.getRuntimeService();

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employee", employee);
		variables.put("nrOfHolidays", nrOfHolidays);
		variables.put("description", description);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);
		/*
		 * 在流程实例启动后，会创建一个执行(execution)，并将其放在启动事件上。
		 * 从这里开始，这个执行沿着顺序流移动到经理审批的用户任务，并执行用户任务行为。
		 * 这个行为将在数据库中创建一个任务，该任务可以之后使用查询找到。
		 * 用户任务是一个等待状态(wait state)，引擎会停止执行，返回API调用处。
		 */
		/*
		 * ACT_HI_VARINST(历史变量)：存放employee、nrOfHolidays、description
		 * ACT_HI_TASKINST（历史任务）：执行的任务  Approve or reject request
		 * ACT_HI_PROCINST（历史流程实例）：本例子：startEvent
		 * ACT_HI_ACTINST（历史动作实例）：本例子：startEvent
		 * ACT_HI_IDENTITYLINK（历史用户）：本例子：managers
		 * ACT_RU_EXECUTION（运行中的执行）：本例子：startEvent
		 * ACT_RU_ACTINST（运行中的动作实例）：本例子：startEvent
		 * ACT_RU_TASK（运行中的任务）：Approve or reject request
		 * ACT_RU_IDENTITYLINK（运行中的用户）：managers
		 * ACT_RU_VARIABLE（运行中的变量）：employee、nrOfHolidays、description
		 */
	}
	/**
	 * 查询与完成任务
	 * @createTime: 2019年9月12日 下午4:17:47
	 * @author: lin.yisong
	 */
	@Test
	public void testQueryAndComplete() {
		//查询任务
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
		System.out.println("You have " + tasks.size() + " tasks:");
		for (int i=0; i<tasks.size(); i++) {
		  System.out.println((i+1) + ") " + tasks.get(i).getName());
		}
		//完成任务
		Scanner scanner= new Scanner(System.in);
		System.out.println("Which task would you like to complete?");
		int taskIndex = Integer.valueOf(scanner.nextLine());
		Task task = tasks.get(taskIndex - 1);
		Map<String, Object> processVariables = taskService.getVariables(task.getId());
		System.out.println(processVariables.get("employee") + " wants " +processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");
		
		boolean approved = scanner.nextLine().toLowerCase().equals("y");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("approved", approved);
		taskService.complete(task.getId(), variables);
	}
	
	@Test
	public void testQueryHistory() {
		HistoryService historyService = processEngine.getHistoryService();
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().list();
		for (HistoricProcessInstance historicProcessInstance : list) {
			List<HistoricActivityInstance> activities =
			  historyService.createHistoricActivityInstanceQuery()
			   .processInstanceId(historicProcessInstance.getId())
			   .finished()
			   .orderByHistoricActivityInstanceEndTime().asc()
			   .list();

			for (HistoricActivityInstance activity : activities) {
			  System.out.println(activity.getActivityId() + " took "
			    + activity.getDurationInMillis() + " milliseconds");
			}
		}
		
	}
	
}
