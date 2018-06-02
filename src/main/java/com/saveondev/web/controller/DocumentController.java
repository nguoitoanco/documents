package com.saveondev.web.controller;

import com.saveondev.entity.Document;
import com.saveondev.service.DocumentService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class DocumentController {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);
    private final DocumentService documentService;

    private final RuntimeService runtimeService;

    private final ServletContext context;

    @Autowired
    public DocumentController(DocumentService documentService, RuntimeService runtimeService, ServletContext context) {
        this.documentService = documentService;
        this.runtimeService = runtimeService;
        this.context = context;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getDocumentList(Model model) {
        List<Document> documents = documentService.findAll();
        model.addAttribute("documentList", documents);
        return "documentList";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (!file.isEmpty()) {
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();

                LOG.info("file.getOriginalFilename:" + file.getOriginalFilename());
                Path path = Paths.get(file.getOriginalFilename());

                LOG.info("Start writing file:");
                path = Files.write(path, bytes);
                LOG.info("Finish writing file:");

                LOG.info("Absolute Written File Path:" + path.toString());

                LOG.info("Save file into Database:");
                Document document = documentService.upload(file, path.toString());
                LOG.info("Finish saving file");

                LOG.info("Start Process");
                ProcessInstance process = this.runtimeService.startProcessInstanceByKey("PublicDocumentProcess");
                runtimeService.setVariable(process.getId(), "document", document);

                LOG.info("Finish Process");
                redirectAttributes.addFlashAttribute("messages", "Upload file successfully");

            } else {
                LOG.info("File is empty");
                redirectAttributes.addFlashAttribute("messages", "Please select a file to upload");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return  "redirect:/";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam(name = "id") Integer id, RedirectAttributes redir) {
        LOG.info("Delete File");
        LOG.info("Doc Id input:" + id);
        Document document = documentService.findById(id);
        File file = new File(document.getPath());
        LOG.info("File Exists:" + file.exists());
        if (file.delete()) {
            file.delete();
        }

        documentService.delete(id);
        return "redirect:/";
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void download(@RequestParam(name = "id") Integer id, HttpServletResponse response)
            throws IOException {
        LOG.info("Download File");
        LOG.info("Doc Id input:" + id);
        Document document = documentService.findById(id);
        File file = new File(document.getPath());
        LOG.info("File Exists:" + file.exists());

        Path path = Paths.get(file.getAbsolutePath());
        byte[] fileBytes = Files.readAllBytes(path);

        ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
        org.apache.commons.io.IOUtils.copy(bis, response.getOutputStream());
        response.flushBuffer();
        org.apache.commons.io.IOUtils.closeQuietly(bis);
    }
}
