package com.dissertation.Meetings.services;

import com.dissertation.Meetings.dtos.BusyTimeDto;
import com.dissertation.Meetings.dtos.BusyTimeRegisterDto;
import com.dissertation.Meetings.dtos.MeetingDto;
import com.dissertation.Meetings.dtos.ParticipantDto;
import com.dissertation.Meetings.entities.*;
import com.dissertation.Meetings.exceptions.BadRequestException;
import com.dissertation.Meetings.exceptions.ResourceNotFoundException;
import com.dissertation.Meetings.mappers.BusyTimeMapper;
import com.dissertation.Meetings.mappers.BusyTimeRegisterMapper;
import com.dissertation.Meetings.repositories.BusyTimeRepository;
import com.dissertation.Meetings.repositories.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dissertation.Meetings.entities.Response.YES;

@Service
public class BusyTimeService {

    @Autowired
    private BusyTimeRepository busyTimeRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private BusyTimeMapper busyTimeMapper;

    @Autowired
    private BusyTimeRegisterMapper busyTimeRegisterMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Autowired
//    private UserService userService;

    private Employee getEmployeeById(Long employeeId, String header) {

        Employee employee = new Employee();
        try {
//            employee = userService.getEmployeeById(employeeId, header);
            WebClient webClient = WebClient.builder()
//                    .baseUrl("localhost:8080/employees/" + employeeId)
                    .baseUrl("authentication:8080/employees/" + employeeId)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, header)
                    .build();
            Mono<Employee> employeeMono = webClient.get().retrieve().bodyToMono(Employee.class);
            employee = employeeMono.block();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Employee with id " + employeeId + " not found!");
        }

        return employee;

    }

    private BusyTimeDto busyTimeToBusyTimeDto (BusyTime busyTime, String header) {

        BusyTimeDto busyTimeDto = busyTimeMapper.mapToDto(busyTime);
        Employee employee = getEmployeeById(busyTime.getEmployeeId(), header);
        busyTimeDto.setEmployee(employee);

        return busyTimeDto;

    }

    private BusyTime busyTimeDtoToBusyTime (BusyTimeDto busyTimeDto, String header) {

        BusyTime busyTime = busyTimeMapper.mapToEntity(busyTimeDto);
        busyTime.setEmployeeId(busyTimeDto.getEmployee().getId());

        return busyTime;

    }
    
    public BusyTimeDto create (BusyTimeRegisterDto busyTimeRegisterDto, String header) {

        BusyTime busyTime = busyTimeRegisterMapper.mapToEntity(busyTimeRegisterDto);

        Employee employee = getEmployeeById(busyTime.getEmployeeId(), header);
        if(!employee.isEnabled()) {
            throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " is disabled!");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate date = LocalDate.parse(busyTimeRegisterDto.getDate(), dateFormatter);
        LocalTime start = LocalTime.parse(busyTimeRegisterDto.getStart(), timeFormatter);
        LocalTime end = LocalTime.parse(busyTimeRegisterDto.getEnd(), timeFormatter);

        if (date.isBefore(LocalDate.now())) {
            throw new BadRequestException("Date " + date + " has been exceeded!");
        }

        if (date.isAfter(LocalDate.now().plusMonths(1))) {
            throw new BadRequestException("Date " + date + " is more than 1 month away!");
        }

        if ((date.getDayOfWeek() == DayOfWeek.SATURDAY) || (date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
            throw new BadRequestException("Date " + date + " is on the weekend!");
        }

        if (start.isBefore(LocalTime.parse("08:00", timeFormatter))) {
            throw new BadRequestException("Start time " + start + " must be after 08:00!");
        }

        if (end.isAfter(LocalTime.parse("20:00", timeFormatter))) {
            throw new BadRequestException("End time " + end + " must be before 20:00!");
        }

        if (date.isEqual(LocalDate.now()) && start.isBefore(LocalTime.now())) {
            throw new BadRequestException("Start time " + start + " has been exceeded!");
        }

        if (end.isBefore(start.plusMinutes(15))) {
            throw new BadRequestException("Busy time must be at least 15 minutes long!");
        }

        if (end.isAfter(start.plusHours(2))) {
            throw new BadRequestException("Busy time must be at last 2 hours long!");
        }

        List<BusyTime> busyTimes = busyTimeRepository.findByDateAndEmployeeId(busyTime.getDate(), busyTime.getEmployeeId());
        if(!busyTimes.isEmpty()) {
            for(BusyTime bt : busyTimes) {
                if(date.isEqual(bt.getDate())) {
                    if (start.isBefore(bt.getStart()) && end.isAfter(bt.getStart())) {
                        throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " is already busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                    } else if ((start.isAfter(bt.getStart()) || start.equals(bt.getStart())) && start.isBefore(bt.getEnd())) {
                        throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " is already busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                    }
                }
            }
        }

        List<Meeting> meetings = meetingRepository.findByDateAndParticipantsEmployeeId(busyTime.getDate(), busyTime.getEmployeeId());
        if (!meetings.isEmpty()) {
            for (Meeting m : meetings) {
                if (date.isEqual(m.getDate())) {
                    Response response = null;
                    for (Participant p : m.getParticipants()) {
                        if (p.getEmployeeId() == busyTime.getEmployeeId()) {
                            response = p.getResponse();
                        }
                    }
                    if (response == YES) {
                        if (start.isBefore(m.getStart()) && end.isAfter(m.getStart())) {
                            throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " has a meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                        } else if ((start.isAfter(m.getStart()) || start.equals(m.getStart())) && start.isBefore(m.getEnd())) {
                            throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " has a meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                        }
                    }
                }
            }
        }

        busyTimeRepository.save(busyTime);

        BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(busyTime, header);

        return busyTimeDto;

    }

    public List<BusyTimeDto> getAll(String header) {

        List<BusyTime> busyTimes = busyTimeRepository.findAllByOrderByDateDescStartDesc();
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime bt : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(bt, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return  busyTimeDtos;

    }

    public BusyTimeDto getById(String id, String header) {

        Optional<BusyTime> busyTimeOptional = busyTimeRepository.findById(id);
        if(busyTimeOptional.isEmpty()) {
            throw new ResourceNotFoundException("Busy time with id " + id + " not found!");
        }
        BusyTime busyTime = busyTimeOptional.get();
        BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(busyTime, header);

        return busyTimeDto;

    }

    public List<BusyTimeDto> getByDate(String dateString, String header) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, dateFormatter);

        List<BusyTime> busyTimes = busyTimeRepository.findByDateOrderByDateAscStartAsc(date);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    public List<BusyTimeDto> getByEmployee(Long employeeId, String header) {

        List<BusyTime> busyTimes = busyTimeRepository.findByEmployeeIdOrderByDateDescStartDesc(employeeId);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    public List<BusyTimeDto> getByDateAndEmployee(String dateString, Long employeeId, String header) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, dateFormatter);

        List<BusyTime> busyTimes = busyTimeRepository.findByDateAndEmployeeIdOrderByDateAscStartAsc(date, employeeId);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    public List<BusyTimeDto> getFutureBusyTimes(String header) {

        LocalDate date = LocalDate.now();
        List<BusyTime> busyTimes = busyTimeRepository.findByDateAfterOrderByDateAscStartAsc(date);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    public List<BusyTimeDto> getPastBusyTimes(String header) {

        LocalDate date = LocalDate.now();
        List<BusyTime> busyTimes = busyTimeRepository.findByDateBeforeOrderByDateDescStartDesc(date);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    public List<BusyTimeDto> getFutureAndRecentPastBusyTimes(String header) {

        LocalDate dateStart = LocalDate.now().minusMonths(1);
        LocalDate dateEnd = LocalDate.now().plusMonths(1);
        List<BusyTime> busyTimes = busyTimeRepository.findByDateBetweenOrderByDateDescStartDesc(dateStart, dateEnd);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    public List<BusyTimeDto> getFutureBusyTimesByEmployee (Long employeeId, String header) {

        LocalDate date = LocalDate.now();
        List<BusyTime> busyTimes = busyTimeRepository.findByDateAfterAndEmployeeIdOrderByDateDescStartAsc(date, employeeId);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    public List<BusyTimeDto> getPastBusyTimesByEmployee (Long employeeId, String header) {

        LocalDate date = LocalDate.now();
        List<BusyTime> busyTimes = busyTimeRepository.findByDateBeforeAndEmployeeIdOrderByDateDescStartDesc(date, employeeId);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    public List<BusyTimeDto> getFutureAndRecentPastBusyTimesByEmployee (Long employeeId, String header) {

        LocalDate dateStart = LocalDate.now().minusMonths(1);
        LocalDate dateEnd = LocalDate.now().plusMonths(1);
        List<BusyTime> busyTimes = busyTimeRepository.findByDateBetweenAndEmployeeIdOrderByDateDescStartDesc(dateStart, dateEnd, employeeId);
        List<BusyTimeDto> busyTimeDtos = new ArrayList<>();
        for (BusyTime m : busyTimes) {
            BusyTimeDto busyTimeDto = busyTimeToBusyTimeDto(m, header);
            busyTimeDtos.add(busyTimeDto);
        }

        return busyTimeDtos;

    }

    @Transactional
    public String delete(String id, String header) {

        Optional<BusyTime> busyTimeOptional = busyTimeRepository.findById(id);
        if(busyTimeOptional.isEmpty()) {
            throw new ResourceNotFoundException("Busy time with id " + id + " not found!");
        }
        BusyTime busyTime = busyTimeOptional.get();

        busyTimeRepository.deleteById(id);

        return "Busy time with id " + id + " deleted!";

    }
    
}
