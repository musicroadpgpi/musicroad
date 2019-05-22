package com.nullpoint.musicroad.web.rest.errors;

public class NumberException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NumberException() {
        super(ErrorConstants.NUMBER_ERROR, "Component number more than 0", "band", "messages.error.CNumber");
    }
}
