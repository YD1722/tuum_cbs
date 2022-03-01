package com.tuum.cbs.beans.common.response;

import com.tuum.cbs.beans.common.ResponseStatus;

public class Response {
    private ResponseStatus status;
    private Object data;
    private String error;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
