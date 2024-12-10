package com.example.scheduler.repository;

import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {
    ScheduleResponseDto saveSchedule(Schedule schedule);
    List<ScheduleResponseDto> findAllSchedules(int page, int size);
    Schedule findScheduleByIdOrElseThrow(Long id);
    int updateSchedule(Long id, String task, String name, String password);
    int deleteSchedule (Long id);

}
