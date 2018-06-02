package com.saveondev.dto;

import com.saveondev.entity.Document;
import lombok.Data;

public class TaskDto {
    private String id;
    private Document document;

    public TaskDto(String id, Document document) {
        this.id = id;
        this.document = document;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
