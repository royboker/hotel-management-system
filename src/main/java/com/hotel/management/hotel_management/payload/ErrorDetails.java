package com.hotel.management.hotel_management.payload;

/**
 * Represents the structure of error details in the hotel management system.
 * This class is used to encapsulate error information that can be returned in API responses.
 */
import java.util.Date;
public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDetails() {
        return details;
    }
}
