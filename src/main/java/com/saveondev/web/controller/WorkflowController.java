package com.saveondev.web.controller;

import com.saveondev.dto.TaskDto;
import com.saveondev.entity.Document;
import com.saveondev.enums.DocumentStatus;
import com.saveondev.service.DocumentService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowController.class);

    private final TaskService taskService;
    private final DocumentService documentService;
    @Autowired
    public WorkflowController(TaskService taskService, DocumentService documentService) {
        this.taskService = taskService;
        this.documentService = documentService;
    }

    @RequestMapping(value="/taskList", method = RequestMethod.GET)
    public String getTaskList(Model model) {
        List<Task> tasks = taskService.createTaskQuery().active().list();
        List<TaskDto> taskDtoList = new ArrayList<>();
        LOG.info("Task List size before:" + tasks.size());
        for (Task task : tasks) {
            try {
                Document doc = (Document) taskService.getVariable(task.getId(), "document");
                LOG.info("Document is not null:" + (doc != null));
                taskDtoList.add(new TaskDto(task.getId(), doc));
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        LOG.info("Task List Size after:" + taskDtoList.size());
        model.addAttribute("tasks", taskDtoList);
        return "tasks";
    }

    @RequestMapping(value="approve", method = RequestMethod.POST)
    public String approve(@RequestParam(name="taskId") String taskId) {
        Document doc = (Document) taskService.getVariable(taskId, "document");
        LOG.info("Workflow: Start Approving");
        this.taskService.complete(
                taskId, Collections.singletonMap("status", DocumentStatus.APPROVED.name()));
        LOG.info("Workflow: Finish Approving");

        doc.setStatus(DocumentStatus.APPROVED.getStatus());
        documentService.update(doc);
        return "redirect:/taskList";
    }

    @RequestMapping(value="reject", method = RequestMethod.POST)
    public String reject(@RequestParam(name="taskId") String taskId) {
        Document doc = (Document) taskService.getVariable(taskId, "document");

        LOG.info("Workflow: Start Rejecting");
        this.taskService.complete(
                taskId, Collections.singletonMap("status", DocumentStatus.REJECTED.name()));
        LOG.info("Workflow: Finish Rejecting");

        doc.setStatus(DocumentStatus.REJECTED.getStatus());
        documentService.update(doc);
        return "redirect:/taskList";
    }
}
