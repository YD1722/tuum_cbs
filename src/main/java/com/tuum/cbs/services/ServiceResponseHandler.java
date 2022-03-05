package com.tuum.cbs.services;

import com.tuum.cbs.beans.common.ResponseStatus;
import com.tuum.cbs.beans.common.response.Response;

public class ServiceResponseHandler {
    public static Response generateErrorResponse() {
        return generateResponse(ResponseStatus.ERROR, null, null);
    }

    public static Response generateErrorResponse(String message) {
        return generateResponse(ResponseStatus.ERROR, null, message);
    }

    public static Response generateResponse(ResponseStatus status, Object responseObj) {
        return generateResponse(status, responseObj, null);
    }

    public static Response generateResponse(ResponseStatus status, Object responseObj, String message) {
        Response response = new Response();

        response.setStatus(status);
        response.setMessage(message);
        response.setData(responseObj);

        return response;
    }
}
