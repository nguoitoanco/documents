package com.saveondev.enums;

public enum DocumentStatus {
    WAITING(1), APPROVED(2), REJECTED(3);
    private Integer status;

    DocumentStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
