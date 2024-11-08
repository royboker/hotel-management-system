package com.hotel.management.hotel_management.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a paginated response for a list of managers.
 * This class is used to encapsulate the response data when querying for manager details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerResponse {
    private List<ManagerDto> content;
    private int pageNo;
    private int pageSize;
    private Long totalManagers;
    private int totalPages;
    private boolean last;
}
