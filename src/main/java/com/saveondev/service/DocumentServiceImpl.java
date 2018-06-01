package com.saveondev.service;

import com.saveondev.entity.Document;
import com.saveondev.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Autowired
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document upload(MultipartFile files, String filePath) {
        Document doc = new Document();
        doc.setPath(filePath);
        return documentRepository.save(doc);
    }

    @Override
    public Document update(Document doc) {
        return documentRepository.save(doc);
    }

    @Override
    public void delete(Integer docId) {
        documentRepository.delete(docId);
    }

}
