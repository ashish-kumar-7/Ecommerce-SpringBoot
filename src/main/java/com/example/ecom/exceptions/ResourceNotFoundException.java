package com.example.ecom.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String field;
    private String fieldName;
    private Long fieldID;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s",resourceName,field,fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldID) {
        super(String.format("%s not found with %s: %d",resourceName,field,fieldID));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldID = fieldID;
    }
}
