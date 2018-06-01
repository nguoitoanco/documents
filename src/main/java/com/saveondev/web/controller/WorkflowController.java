package com.saveondev.web.controller;

import com.saveondev.dto.TaskDto;
import com.saveondev.entity.Document;
import com.saveondev.enums.DocumentStatus;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class WorkflowController {
    private final TaskService taskService;

    @Autowired
    public WorkflowController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value="/taskList", method = RequestMethod.GET)
    public String approveTask(Model model) {
        List<Task> tasks = taskService.createTaskQuery().active().list();
        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task : tasks) {
            Document doc = (Document) taskService.getVariable(task.getId(), "document");
            taskDtoList.add(new TaskDto(task.getId(), doc));
        }

        model.addAttribute("tasks", taskDtoList);
        return "tasks";
    }

    @RequestMapping(value="approve", method = RequestMethod.POST)
    public String approveTask(@RequestParam(name="taskId") String taskId) {
        this.taskService.complete(
                taskId, Collections.singletonMap("status", DocumentStatus.APPROVED.name()));
        return "redirect:/tasks";
    }

    @RequestMapping(value="reject", method = RequestMethod.POST)
    public String rejectTask(@RequestParam(name="taskId") String taskId) {
        this.taskService.complete(
                taskId, Collections.singletonMap("status", DocumentStatus.REJECTED.name()));
        return "redirect:/tasks";
    }
}
