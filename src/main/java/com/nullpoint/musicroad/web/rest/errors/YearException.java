package com.nullpoint.musicroad.web.rest.errors;

public class YearException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public YearException() {
        super(ErrorConstants.YEAR_ERROR, "Year in Past!", "band", "messages.error.year");
    }
}
