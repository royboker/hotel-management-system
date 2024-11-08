package com.hotel.management.hotel_management.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A response wrapper for paginated results of Task data.
 * This class is used to encapsulate the task data returned by the service along with
 * pagination details for client-side processing.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse
{
    private List<TaskDto> content;
    private int pageNo;
    private int pageSize;
    private Long totalTasks;
    private int totalPages;
    private boolean last;
}
