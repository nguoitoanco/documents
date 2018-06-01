package com.saveondev.service;

import com.saveondev.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    List<Document> findAll();
    Document upload(MultipartFile files, String path);

    Document update(Document doc);
    void delete(Integer docId);

}
