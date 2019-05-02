package com.nullpoint.musicroad.web.rest.errors;

public class BandNameException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public BandNameException() {
        super(ErrorConstants.CONSTRAINT_VIOLATION_TYPE, "Band name can't be blank!", "band", "bandNameError");
    }
}
