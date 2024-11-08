package com.hotel.management.hotel_management.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)//Respond with HtTTP status code whenever exception is thtown
public class ResourceNotFoundException extends RuntimeException
{
    private String resourceName;
    private String fieldName;
    private Long fieldValue;

    public ResourceNotFoundException( String resourceName, String fieldName, Long fieldValue) {
        super(String.format("%s not found with %s : '%s'",resourceName,fieldName,fieldValue));//post not found with id : 1
        this.fieldName = fieldName;
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getFieldValue() {
        return fieldValue;
    }
}
