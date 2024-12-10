package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);
    List<ScheduleResponseDto> findAllSchedules(int page, int size);
    ScheduleResponseDto findScheduleById(Long id);
    ScheduleResponseDto updateSchedule(Long id, String task, String name, String password);
    void deleteSchedule(Long id);
}
