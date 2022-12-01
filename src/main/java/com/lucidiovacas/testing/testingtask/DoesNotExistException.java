package com.lucidiovacas.testing.testingtask;

public class DoesNotExistException extends Exception {

    private String resource;

    public DoesNotExistException() {
    }

    public DoesNotExistException(String resource, String message) {
        super(message);
        this.resource = resource;
    }

    public DoesNotExistException(Throwable cause) {
        super(cause);
    }

    public String getResource() {
        return this.resource;
    }
}
