package com.example.scheduler.controller;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    /**
     * 일정 전체 조회 API
     *
     * @return 일정 전체 List형태로 JSON 응답
     */
    @GetMapping()
    public List<ScheduleResponseDto> findAllSchedules(
            // 1페이지 부터 시작
            @RequestParam(defaultValue = "1") int page,
            // 사이즈 크기는 10
            @RequestParam(defaultValue = "10") int size
    ) {
        return scheduleService.findAllSchedules(page, size);
    }


    /**
     * 일정 단건 조회 API
     *
     * @param id 일정 고유 식별자
     * @return : {@link ResponseEntity<ScheduleResponseDto>} JSON 응답
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    /**
     * 일정 수정 API
     *
     * @param id 일정 고유 식별자
     * @param :  {@link ScheduleRequestDto} 메모 생성 요청 객체
     * @return : {@link ResponseEntity<ScheduleResponseDto>} JSON 응답
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto.getTask(), dto.getName(), dto.getPassword()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id
    ) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

