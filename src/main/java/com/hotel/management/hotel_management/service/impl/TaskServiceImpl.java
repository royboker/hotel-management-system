package com.hotel.management.hotel_management.service.impl;

import com.hotel.management.hotel_management.entity.Task;
import com.hotel.management.hotel_management.enums.TaskPriority;
import com.hotel.management.hotel_management.enums.TaskStatus;
import com.hotel.management.hotel_management.enums.Type;
import com.hotel.management.hotel_management.exception.ResourceNotFoundException;
import com.hotel.management.hotel_management.payload.TaskDto;
import com.hotel.management.hotel_management.payload.TaskResponse;
import com.hotel.management.hotel_management.repository.TaskRepository;
import com.hotel.management.hotel_management.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing tasks in the hotel management system.
 * Provides functionality for creating, updating, retrieving, and deleting tasks,
 * as well as searching and filtering tasks by priority and type.
 */
@Service
public class TaskServiceImpl implements TaskService
{
    private TaskRepository taskRepository;
    private ModelMapper mapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,ModelMapper mapper) {
        this.taskRepository = taskRepository;
        this.mapper=mapper;
    }


    @Override
    public TaskDto createTask(TaskDto taskDto)
    {
        Task task = this.mapToEntity(taskDto);

        task.setTaskStatus(TaskStatus.NOT_STARTED);

        Task newTask = taskRepository.save(task);

        TaskDto taskResponce = this.mapToDto(newTask);

        return taskResponce;
    }

    @Override
    public TaskResponse getAllTasks(int pageNo,int pageSize,String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //create pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Task> tasks =  taskRepository.findAll(pageable);


        List<Task> taskList = tasks.getContent();
        ;

        List<TaskDto> content =  taskList.stream().map(task -> mapToDto(task)).collect(Collectors.toList());


        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setContent(content);
        taskResponse.setPageNo(tasks.getNumber());
        taskResponse.setPageSize(tasks.getSize());
        taskResponse.setTotalTasks(tasks.getTotalElements());
        taskResponse.setTotalPages(tasks.getTotalPages());
        taskResponse.setLast(tasks.isLast());

        return taskResponse;
    }

    @Override
    public TaskDto getTaskById(Long id)
    {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task","id",id));
        return mapToDto(task);
    }

    @Override
    public TaskDto upadteTask(TaskDto taskDto, Long id)
    {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task","id",id));
        task.setTaskPriority(taskDto.getTaskPriority());
        task.setTaskType(taskDto.getTaskType());
        task.setTaskTitle(taskDto.getTaskTitle());
        task.setTaskDescription(taskDto.getTaskDescription());
        task.setTaskStatus(taskDto.getTaskStatus());
        task.setLastUpdate(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);
        return mapToDto(updatedTask);

    }

    @Override
    public void deleteTaskById(Long id)
    {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task","id",id));
        taskRepository.delete(task);
    }


    @Override
    public List<TaskDto> getTasksByPriority(TaskPriority priority)
    {
        List<Task> tasksByPriority = taskRepository.findByTaskPriority(priority);
        return tasksByPriority.stream().map(task -> mapToDto(task)).collect(Collectors.toList());

    }

    @Override
    public List<TaskDto> getTasksByType(Type type) {
        List<Task> tasksByType = taskRepository.findByTaskType(type);
        return tasksByType.stream().map(task -> mapToDto(task)).collect(Collectors.toList());

    }

    @Override
    public List<TaskDto> searchTasks(String query)
    {
       List<Task> tasks = taskRepository.searchTasks(query);
       return tasks.stream().map(task -> mapToDto(task)).collect(Collectors.toList());
    }

    //convert Entity to DTO
    private TaskDto mapToDto(Task task)
    {
        TaskDto taskDto = mapper.map(task,TaskDto.class);
        return taskDto;
    }

    //convert DTO to Entity
    private Task mapToEntity(TaskDto taskDto)
    {
        Task task = mapper.map(taskDto,Task.class);
        return task;
    }


}
