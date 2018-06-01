package com.saveondev.web.controller;

import com.saveondev.entity.Document;
import com.saveondev.service.DocumentService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class DocumentController {

    private final DocumentService documentService;

    private final RuntimeService runtimeService;

    private final ServletContext context;

    @Autowired
    public DocumentController(DocumentService documentService, RuntimeService runtimeService, ServletContext context) {
        this.documentService = documentService;
        this.runtimeService = runtimeService;
        this.context = context;
    }

    @RequestMapping("/")
    public String getDocumentList(Model model) {
        List<Document> documents = documentService.findAll();
        model.addAttribute("documentList", documents);
        return "documentList";
    }

    @RequestMapping(value = "uploadView", method = RequestMethod.GET)
    public String uploadView() {
        return "uploadView";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (!file.isEmpty()) {
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                String relativeWebPath = "./";
                String absoluteFilePath = context.getRealPath(relativeWebPath);
                Path path = Paths.get(relativeWebPath + file.getOriginalFilename());
                Files.write(path, bytes);

                Document document = documentService.upload(file, path.toString());
                ProcessInstance process = this.runtimeService.startProcessInstanceByKey("PublicDocumentProcess");
                runtimeService.setVariable(process.getId(), "document", document);
                redirectAttributes.addFlashAttribute("message", "Upload file successfully");

            } else {
                redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "documentList";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(name = "id") Integer id, RedirectAttributes redir) {
        documentService.delete(id);
        return "redirect:/";
    }

}
