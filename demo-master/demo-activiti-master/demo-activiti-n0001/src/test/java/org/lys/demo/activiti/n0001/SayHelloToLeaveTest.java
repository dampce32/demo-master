package org.lys.demo.activiti.n0001;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SayHelloToLeaveTest {

    @Test
    public void testStartProcess() throws Exception {
    	/*
    	 * 取得流程引擎
    	 * 
    	 * 后续的各种服务都是从流程引擎中取得服务，如
    	 * RepositoryService repositoryService = processEngine.getRepositoryService();
    	 */
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();
        /*
         * 取得仓库服务RepositoryService（负责将流程定义文件部署到数据库，生成流程定义）
         * 
         * 流程定义：告诉引擎流程的流转信息，类比：请假规则
         */
        RepositoryService repositoryService = processEngine.getRepositoryService();
        String bpmnFileName = "helloworld/SayHelloToLeave.bpmn";
        /*
         * addInputStream(String resourceName, InputStream inputStream) 
         */
        repositoryService
                .createDeployment()
                .addInputStream(
                        "SayHelloToLeave.bpmn",
                        this.getClass().getClassLoader()
                                .getResourceAsStream(bpmnFileName)).deploy();
        //取得流程定义
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery().singleResult();
        assertEquals("SayHelloToLeave", processDefinition.getKey());
        /*
         * RuntimeService：
         * 
         */
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyUser", "employee1");
        variables.put("days", 3);
        /*
         * 启动流程实例（类比：用户提交了一个请假申请单）
         * 
         */
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "SayHelloToLeave", variables);
        
        assertNotNull(processInstance);
        
        System.out.println("pid=" + processInstance.getId() + ", pdid="
                + processInstance.getProcessDefinitionId());
        /*
         * TaskService：任务Service
         * 
         * 查询当前任务
         * 
         */
        TaskService taskService = processEngine.getTaskService();
        Task taskOfDeptLeader = taskService.createTaskQuery()
                .taskCandidateGroup("deptLeader").singleResult();
        assertNotNull(taskOfDeptLeader);
        assertEquals("领导审批", taskOfDeptLeader.getName());

        //签收：一个任务如果是一个角色完成涉及多个人，如果一个角色用户签收，相同角色的其他用户的当前任务中则没有该任务了
        taskService.claim(taskOfDeptLeader.getId(), "leaderUser");
        variables = new HashMap<String, Object>();
        variables.put("approved", true);
        taskService.complete(taskOfDeptLeader.getId(), variables);//完成任务

        taskOfDeptLeader = taskService.createTaskQuery()
                .taskCandidateGroup("deptLeader").singleResult();
        assertNull(taskOfDeptLeader);
        /*
         * HistoryService：历史Service
         * 
         * 查询任务的完成历史
         */
        HistoryService historyService = processEngine.getHistoryService();
        long count = historyService.createHistoricProcessInstanceQuery().finished()
                .count();
        assertEquals(1, count);
    }
}