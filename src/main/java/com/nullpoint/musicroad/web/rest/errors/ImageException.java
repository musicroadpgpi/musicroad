package com.nullpoint.musicroad.web.rest.errors;

public class ImageException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public ImageException() {
        super(ErrorConstants.IMAGE_ERROR, "Image can't be null", "band", "imageerror");
    }
}
