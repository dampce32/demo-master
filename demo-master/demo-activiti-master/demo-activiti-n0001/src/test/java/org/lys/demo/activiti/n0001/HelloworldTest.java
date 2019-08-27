package org.lys.demo.activiti.n0001;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloworldTest {

	Logger logger = LoggerFactory.getLogger(HelloworldTest.class);
	private Scanner scanner;

	@Test
	public void testHelloworld() {
		logger.info("启动程序");
		//创建流程引擎
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		ProcessEngine processEngine = cfg.buildProcessEngine();
		String name = processEngine.getName();
		String version = ProcessEngine.VERSION;
		logger.info("流程引擎名称{}，版本{}",name,version);
		//部署流程
        RepositoryService repositoryService = processEngine.getRepositoryService();
        String bpmnFileName = "helloworld/helloworld.bpmn";
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addInputStream("helloworld.bpmn",this.getClass().getClassLoader().getResourceAsStream(bpmnFileName))
                .deploy();
        //取得流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        logger.info("部署流程,并取得流程定义，流程定义Key:{}",processDefinition.getKey());
        assertEquals("helloworld", processDefinition.getKey());//helloworld.bpmn文件中定义的id
		//启动流程，生成流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloworld");
        logger.info("启动流程，生成流程实例，流程实例Id:{}，创建时间：{}",processInstance.getId(),processInstance.getStartTime());
        //处理任务
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().list();
        while(list.size()!=0) {
        	logger.info("待处理的任务数：{}",list.size());
            scanner = new Scanner(System.in);
            for (Task task : list) {
            	logger.info("正在处理任务：{}",task.getName());
            	Map<String,Object> variables = new HashMap<String, Object>();
            	//使用表单信息，指导任务完成
            	FormService formService = processEngine.getFormService();
            	TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            	List<FormProperty> formProperties = taskFormData.getFormProperties();
            	for (FormProperty formProperty : formProperties) {
            		logger.info("请输入：{}",formProperty.getName());
            		String nextLine = scanner.nextLine();
            		variables.put(formProperty.getId(), nextLine);
    			}
            	 taskService.complete(task.getId(), variables);
    		}
            list = taskService.createTaskQuery().list();
        }
		logger.info("结束程序");
	}
}
