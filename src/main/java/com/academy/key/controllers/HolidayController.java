package com.academy.key.controllers;

import com.academy.key.DTOs.HolidayRequestDTO;
import com.academy.key.DTOs.RequestSuccessDTO;
import com.academy.key.DTOs.TaskDTO;
import com.academy.key.services.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/process", produces = MediaType.APPLICATION_JSON_VALUE)
public class HolidayController {

    public HolidayService holidayService;

    @Autowired
    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startHolidayProcess(@RequestBody HolidayRequestDTO holidayRequestDTO) {

        log.info("======= Creating Holiday Request with details {} =======", holidayRequestDTO);

        holidayService.startProcess(holidayRequestDTO);
        return ResponseEntity
                .ok(new RequestSuccessDTO("You application was created succesfully, you will be notified for the status"));

    }

    @GetMapping("/tasks/{assignee}")
    public ResponseEntity<?> getTasks(@PathVariable String assignee) {

        log.info("======= Getting all tasks for assignee {} =======", assignee);
        List<TaskDTO> taskDTOList= new LinkedList<>();
        holidayService.getTasksFor(assignee).forEach(task -> {
            taskDTOList.add(new TaskDTO(task.getId(), task.getName()));
        });

        return ResponseEntity.ok(taskDTOList);
    }

    @PostMapping("/complete/{taskId}/{approved}")
    public ResponseEntity<?> completeTask(@PathVariable String taskId, @PathVariable Boolean approved) {

        log.info("======= Completing task with ID {} and with approval {}", taskId, approved);

        holidayService.completeTask(taskId, approved);
        return ResponseEntity
                .ok()
                .build();
    }

}
