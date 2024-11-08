package com.hotel.management.hotel_management.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response wrapper for paginated results of employee data in the hotel management system.
 * This class encapsulates the content and metadata for the paginated employee data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private List<EmployeeDto> content;
    private int pageNo;
    private int pageSize;
    private Long totalEmployee;
    private int totalPages;
    private boolean last;
}
