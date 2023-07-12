package com.uk.bootintegrationall.springmvc.exception;

/**
 * @Description TODO
 */
public class StorageFileNotFoundException extends StorageException{
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
