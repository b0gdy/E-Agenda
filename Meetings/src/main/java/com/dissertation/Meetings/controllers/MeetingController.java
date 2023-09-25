package com.dissertation.Meetings.controllers;

import com.dissertation.Meetings.dtos.MeetingDto;
import com.dissertation.Meetings.dtos.MeetingRegisterDto;;
import com.dissertation.Meetings.entities.Response;
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
@RequestMapping("/meetings")
@CrossOrigin
@Validated
public class MeetingController {

    @Autowired
    MeetingService meetingService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<MeetingDto> create(@Valid @RequestBody MeetingRegisterDto meetingRegisterDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        MeetingDto createdMeeting = meetingService.create(meetingRegisterDto, header);

        return ResponseEntity.created(URI.create("/meetings" + createdMeeting.getId())).body(createdMeeting);

    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<MeetingDto>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getAll(header));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<MeetingDto> getById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getById(id, header));

    }

    @GetMapping("/get-by-date/{date}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<MeetingDto>> getByDate(@PathVariable String date, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getByDate(date, header));

    }

    @GetMapping("/get-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<MeetingDto>> getByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getByEmployee(employeeId, header));

    }

    @GetMapping("/get-by-date-and-employee")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<MeetingDto>> getByDateAndEmployee(@RequestParam String date, @RequestParam Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getByDateAndEmployee(date, employeeId, header));

    }

    @GetMapping("/get-by-date-and-response-and-employee")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<MeetingDto>> getByDateAndResponseAndEmployee(@RequestParam String date, @RequestParam Response response, @RequestParam Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getByDateAndResponseAndEmployee(date, response, employeeId, header));

    }

    @GetMapping("/get-future-meetings")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<MeetingDto>> getFutureMeetings(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getFutureMeetings(header));

    }

    @GetMapping("/get-past-meetings")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<MeetingDto>> getPastMeetings(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getPastMeetings(header));

    }

    @GetMapping("/get-future-and-recent-past-meetings")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<MeetingDto>> getFutureAndRecentPastMeetings(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getFutureAndRecentPastMeetings(header));

    }

    @GetMapping("/get-future-meetings-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<MeetingDto>> getFutureMeetingsByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getFutureMeetingsByEmployee(employeeId, header));

    }

    @GetMapping("/get-past-meetings-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<MeetingDto>> getPastMeetingsByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getPastMeetingsByEmployee(employeeId, header));

    }

    @GetMapping("/get-future-and-recent-past-meetings-by-employee/{employeeId}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<List<MeetingDto>> getFutureAndRecentPastMeetingsByEmployee(@PathVariable Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.getFutureAndRecentPastMeetingsByEmployee(employeeId, header));

    }

    @PutMapping("/change-date/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<MeetingDto> changeDate(@PathVariable String id, @RequestParam String date, @RequestParam String start, @RequestParam String end, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.changeDate(id, date, start, end, header));

    }

    @PutMapping("/add-participant/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<MeetingDto> addParticipant(@PathVariable String id, @RequestParam Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.addParticipant(id, employeeId, header));

    }

    @PutMapping("/remove-participant/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<MeetingDto> removeParticipant(@PathVariable String id, @RequestParam Long employeeId, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.removeParticipant(id, employeeId, header));

    }

    @PutMapping("/add-response/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('employee')")
    public ResponseEntity<MeetingDto> addResponse(@PathVariable String id, @RequestParam Long employeeId, @RequestParam Response response, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.addResponse(id, employeeId, response, header));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> delete(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        return ResponseEntity.ok().body(meetingService.delete(id, header));

    }

}
