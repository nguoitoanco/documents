package com.saveondev.dto;

import com.saveondev.entity.Document;

public class TaskDto {
    private String id;
    private Document document;

    public TaskDto(String id, Document document) {
        this.id = id;
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
