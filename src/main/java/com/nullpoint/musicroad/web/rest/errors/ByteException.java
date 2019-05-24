package com.nullpoint.musicroad.web.rest.errors;

public class ByteException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public ByteException() {
        super(ErrorConstants.BYTE_ERROR, "Image is pesada", "band", "messages.error.pictureB");
    }
}