package com.dissertation.Meetings.controllers;

import com.dissertation.Meetings.dtos.BusyTimeDto;
import com.dissertation.Meetings.dtos.BusyTimeRegisterDto;
import com.dissertation.Meetings.dtos.MeetingDto;
import com.dissertation.Meetings.entities.Response;
import com.dissertation.Meetings.services.BusyTimeService;
import com.dissertation.Meetings.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/busy-times")
@CrossOrigin
@Validated
public class BusyTimeController {

    @Autowired
    BusyTimeService busyTimeService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<BusyTimeDto> create(@Valid @RequestBody BusyTimeRegisterDto busyTimeRegisterDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        BusyTimeDto createdBusyTime = busyTimeService.create(busyTimeRegisterDto, header);

        return ResponseEntity.created(URI.create("/busyTimes" + createdBusyTime.getId())).body(createdBusyTime);

    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BusyTimeDto>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getAll(header));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<BusyTimeDto> getById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getById(id, header));

    }

    @GetMapping("/get-by-date/{date}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BusyTimeDto>> getByDate(@PathVariable String date, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getByDate(date, header));

    }

    @GetMapping("/get-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<BusyTimeDto>> getByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getByEmployee(employeeId, header));

    }

    @GetMapping("/get-by-date-and-employee")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BusyTimeDto>> getByDateAndEmployee(@RequestParam String date, @RequestParam Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getByDateAndEmployee(date, employeeId, header));

    }

    @GetMapping("/get-future-busy-times")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BusyTimeDto>> getFutureBusyTimes(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getFutureBusyTimes(header));

    }

    @GetMapping("/get-past-busy-times")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BusyTimeDto>> getPastBusyTimes(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getPastBusyTimes(header));

    }

    @GetMapping("/get-future-and-recent-past-busy-times")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<BusyTimeDto>> getFutureAndRecentPastBusyTimes(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getFutureAndRecentPastBusyTimes(header));

    }

    @GetMapping("/get-future-busy-times-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<BusyTimeDto>> getFutureBusyTimesByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getFutureBusyTimesByEmployee(employeeId, header));

    }

    @GetMapping("/get-past-busy-times-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<BusyTimeDto>> getPastBusyTimesByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getPastBusyTimesByEmployee(employeeId, header));

    }

    @GetMapping("/get-future-and-recent-past-busy-times-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<BusyTimeDto>> getFutureAndRecentPastBusyTimesByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.getFutureAndRecentPastBusyTimesByEmployee(employeeId, header));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<String> delete(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(busyTimeService.delete(id, header));

    }
    
}
