package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Author;
import com.example.scheduler.entity.Schedule;
import com.example.scheduler.repository.AuthorRepository;
import com.example.scheduler.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final AuthorRepository authorRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, AuthorRepository authorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        // 이름과 이메일로 작성자 ID 조회
        Long authorId = authorRepository.findAuthorIdByNameAndEmail(dto.getName(), dto.getEmail());

        // 작성자가 없으면 새로 저장
        if (authorId == null) {
            Author newAuthor = new Author(dto.getName(), dto.getEmail());
            authorId = authorRepository.saveAuthor(newAuthor);
        }

        // 일정 저장
        Schedule schedule = new Schedule(dto.getTask(), authorId, dto.getName(), dto.getPassword());
        return scheduleRepository.saveSchedule(schedule);
    }


    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteSchedule(id);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, String task, String name, String password) {
        int rowsAffected = scheduleRepository.updateSchedule(id, task, name, password);
        if (rowsAffected == 0) {
            throw new RuntimeException("Update failed: Invalid ID or password.");
        }
        return findScheduleById(id);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllSchedules();
    }
}
