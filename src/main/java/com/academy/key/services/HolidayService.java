package com.academy.key.services;

import com.academy.key.DTOs.HolidayRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HolidayService {

    private RuntimeService      runtimeService;
    private RepositoryService   repositoryService;
    private TaskService         taskService;

    @Autowired
    public HolidayService(RuntimeService runtimeService,
                          TaskService taskService,
                          RepositoryService repositoryService) {
        this.runtimeService     = runtimeService;
        this.taskService        = taskService;
        this.repositoryService  = repositoryService;
    }

    @Transactional
    public void deployProcessInstance(String processInstanceName) {
        repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();
    }

    @Transactional
    public void startProcess(HolidayRequestDTO holidayRequestDTO) {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("employee", holidayRequestDTO.getEmployeeName());
        processVariables.put("numberOfHolidays", String.valueOf(holidayRequestDTO.getNumberOfHolidays()));
        processVariables.put("phoneNumber", holidayRequestDTO.getPhoneNumber());
        processVariables.put("description", holidayRequestDTO.getDescription());

        log.info("======= Starting process in engine with key {} =======",
                holidayRequestDTO.getProcessInstanceKey());

        runtimeService.startProcessInstanceByKey(holidayRequestDTO.getProcessInstanceKey(), processVariables);
    }

    @Transactional
    public List<Task> getTasksFor(String assignee) {

        log.info("======= Getting tasks for {} =======", assignee);
        List<Task> tasks = taskService
                            .createTaskQuery()
                            .taskCandidateGroup(assignee)
                            .list();

        for (Task task : tasks) {
            log.info("======= Tasks for managers group {} =======",
                    task.toString()); // just list the tasks in console to see for demo purposes
        }
        return tasks;
    }

    // completes a task, in our case that is it can deny or approve a holiday request based on approved value
    @Transactional
    public void completeTask(String taskId, Boolean approved) {

        Task taskToBeCompleted = taskService // optional you can just pass the taskId to the below one
                .createTaskQuery()
                .taskId(taskId)
                .singleResult();

        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("approved", approved); // approved is a process instance variable
        taskService.complete(taskToBeCompleted.getId(), processVariables);
    }

}
