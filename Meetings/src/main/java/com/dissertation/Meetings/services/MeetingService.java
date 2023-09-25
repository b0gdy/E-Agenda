package com.dissertation.Meetings.services;

import com.dissertation.Meetings.dtos.MeetingDto;
import com.dissertation.Meetings.dtos.MeetingRegisterDto;
import com.dissertation.Meetings.dtos.ParticipantDto;
import com.dissertation.Meetings.entities.*;
import com.dissertation.Meetings.exceptions.BadRequestException;
import com.dissertation.Meetings.exceptions.ResourceNotFoundException;
import com.dissertation.Meetings.mappers.MeetingMapper;
import com.dissertation.Meetings.mappers.MeetingRegisterMapper;
import com.dissertation.Meetings.mappers.ParticipantMapper;
import com.dissertation.Meetings.repositories.BusyTimeRepository;
import com.dissertation.Meetings.repositories.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dissertation.Meetings.entities.Response.NO;
import static com.dissertation.Meetings.entities.Response.YES;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private MeetingRegisterMapper meetingRegisterMapper;

    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private BusyTimeRepository busyTimeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Autowired
//    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

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

    private ParticipantDto participantToParticipantDto (Participant participant, String header) {
        
        ParticipantDto participantDto = participantMapper.mapToDto(participant);
        Employee employee = getEmployeeById(participant.getEmployeeId(), header);
        participantDto.setEmployee(employee);
        
        return participantDto;
        
    }

    private Participant participantDtoToParticipant (ParticipantDto participantDto, String header) {

        Participant participant = participantMapper.mapToEntity(participantDto);
        participant.setEmployeeId(participantDto.getEmployee().getId());

        return participant;

    }

    private MeetingDto meetingToMeetingDto (Meeting meeting, String header) {

        MeetingDto meetingDto = meetingMapper.mapToDto(meeting);
        List<Participant> participants = meeting.getParticipants();
        List<ParticipantDto> participantDtos = new ArrayList<>();
        for (Participant p : participants) {
            ParticipantDto participantDto = participantToParticipantDto(p, header);
            participantDtos.add(participantDto);
        }
        meetingDto.setParticipantDtos(participantDtos);

        return meetingDto;

    }

    private Meeting meetingDtoToMeeting (MeetingDto meetingDto, String header) {

        Meeting meeting = meetingMapper.mapToEntity(meetingDto);
        List<ParticipantDto> participantDtos = meetingDto.getParticipantDtos();
        List<Participant> participants = new ArrayList<>();
        for (ParticipantDto p : participantDtos) {
            Participant participant = participantDtoToParticipant(p, header);
            participants.add(participant);
        }
        meeting.setParticipants(participants);

        return meeting;

    }

    private MeetingRegisterDto meetingToMeetingRegisterDto (Meeting meeting, String header) {

        MeetingRegisterDto meetingRegisterDto = meetingRegisterMapper.mapToDto(meeting);
        List<Participant> participants = meeting.getParticipants();
        List<Long> participantIds = new ArrayList<>();
        for (Participant p : participants) {
            Long participantId = p.getEmployeeId();
            participantIds.add(participantId);
        }
        meetingRegisterDto.setParticipantIds(participantIds);

        return meetingRegisterDto;

    }

    private Meeting meetingRegisterDtoToMeeting (MeetingRegisterDto meetingRegisterDto, String header) {

        Meeting meeting = meetingRegisterMapper.mapToEntity(meetingRegisterDto);
        List<Long> participantIds = meetingRegisterDto.getParticipantIds();
        List<Participant> participants = new ArrayList<>();
        for(Long p : participantIds) {
            Participant participant = new Participant();
            participant.setResponse(NO);
            participant.setEmployeeId(p);
            participants.add(participant);
        }
        meeting.setParticipants(participants);

        return meeting;

    }
    
    public MeetingDto create (MeetingRegisterDto meetingRegisterDto, String header) {

        Meeting meeting = meetingRegisterDtoToMeeting(meetingRegisterDto, header);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate date = LocalDate.parse(meetingRegisterDto.getDate(), dateFormatter);
        LocalTime start = LocalTime.parse(meetingRegisterDto.getStart(), timeFormatter);
        LocalTime end = LocalTime.parse(meetingRegisterDto.getEnd(), timeFormatter);

        if (date.isBefore(LocalDate.now())) {
            throw new BadRequestException("Meeting date " + date + " has been exceeded!");
        }

        if (date.isAfter(LocalDate.now().plusMonths(1))) {
            throw new BadRequestException("Meeting date " + date + " is more than 1 month away!");
        }

        if ((date.getDayOfWeek() == DayOfWeek.SATURDAY) || (date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
            throw new BadRequestException("Meeting date " + date + " is on the weekend!");
        }

        if (start.isBefore(LocalTime.parse("08:00", timeFormatter))) {
            throw new BadRequestException("Meeting start time " + start + " must be after 08:00!");
        }

        if (end.isAfter(LocalTime.parse("20:00", timeFormatter))) {
            throw new BadRequestException("Meeting end time " + end + " must be before 20:00!");
        }

        if (date.isEqual(LocalDate.now()) && start.isBefore(LocalTime.now())) {
            throw new BadRequestException("Meeting start time " + start + " has been exceeded!");
        }

        if (end.isBefore(start.plusMinutes(15))) {
            throw new BadRequestException("Meeting must be at least 15 minutes long!");
        }

        if (end.isAfter(start.plusHours(2))) {
            throw new BadRequestException("Meeting must be at last 2 hours long!");
        }

        List<Participant> participants = meeting.getParticipants();
        if(!participants.isEmpty()) {
            for (Participant p : participants) {

                Employee e2 = getEmployeeById(p.getEmployeeId(), header);
                if(!e2.isEnabled()) {
                    throw new BadRequestException("Employee " + e2.getFirstName() + " " + e2.getLastName() + " is disabled!");
                }

                List<Meeting> meetings = meetingRepository.findByDateAndParticipantsEmployeeId(date, p.getEmployeeId());
                if (!meetings.isEmpty()) {
                    for (Meeting m : meetings) {
                        if (date.isEqual(m.getDate())) {
                            Response response = null;
                            for (Participant p2 : m.getParticipants()) {
                                if (p2.getEmployeeId() == p.getEmployeeId()) {
                                    response = p2.getResponse();
                                }
                            }
                            if (response == YES) {
                                if (start.isBefore(m.getStart()) && end.isAfter(m.getStart())) {
                                    Employee e = getEmployeeById(p.getEmployeeId(), header);
                                    throw new BadRequestException("Employee " + e.getFirstName() + " " + e.getLastName() + " has another meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                                } else if ((start.isAfter(m.getStart()) || start.equals(m.getStart())) && start.isBefore(m.getEnd())) {
                                    Employee e = getEmployeeById(p.getEmployeeId(), header);
                                    throw new BadRequestException("Employee " + e.getFirstName() + " " + e.getLastName() + " has another meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                                }
                            }
                        }
                    }
                }

                List<BusyTime> busyTimes = busyTimeRepository.findByDateAndEmployeeId(date, p.getEmployeeId());
                if (!busyTimes.isEmpty()) {
                    for (BusyTime bt : busyTimes) {
                        if (date.isEqual(bt.getDate())) {
                            if (start.isBefore(bt.getStart()) && end.isAfter(bt.getStart())) {
                                Employee e = getEmployeeById(p.getEmployeeId(), header);
                                throw new BadRequestException("Employee " + e.getFirstName() + " " + e.getLastName() + " is busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                            } else if ((start.isAfter(bt.getStart()) || start.equals(bt.getStart())) && start.isBefore(bt.getEnd())) {
                                Employee e = getEmployeeById(p.getEmployeeId(), header);
                                throw new BadRequestException("Employee " + e.getFirstName() + " " + e.getLastName() + " is busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                            }
                        }
                    }
                }

            }
        }

        LocalDateTime createdAt = LocalDateTime.now();
        meeting.setCreatedAt(createdAt);

        LocalDateTime updatedAt = LocalDateTime.now();
        meeting.setUpdatedAt(updatedAt);

//        for (Participant p : meeting.getParticipants()) {
//            p.setResponse(YES);
//        }

        meetingRepository.save(meeting);

        MeetingDto meetingDto = meetingToMeetingDto(meeting, header);



        for(ParticipantDto participantDto : meetingDto.getParticipantDtos()) {

            if(participantDto.getEmployee().getUserName().equals("employee744@gmail.com")) {

                try {

                    Email email = new Email();

                    SimpleMailMessage mailMessage = new SimpleMailMessage();

                    mailMessage.setFrom(sender);

                    email.setRecipient(participantDto.getEmployee().getUserName());
                    mailMessage.setTo(email.getRecipient());

                    email.setSubject("Meeting " + meetingDto.getDate());
                    mailMessage.setSubject(email.getSubject());

                    String body = "";
                    body = body + "Hey!" + "\n" + "New meeting!\n\n" +
                            "Date: " + meetingDto.getDate() + "\n" +
                            "Start: " + meetingDto.getStart() + "\n" +
                            "End: " + meetingDto.getEnd() + "\n\n" +
                            "Participants:" + "\n";
                    for (ParticipantDto p : meetingDto.getParticipantDtos()) {
                        body = body + "\n" +
                                "name: " + p.getEmployee().getFirstName() + " " + p.getEmployee().getLastName() + "\n";
                    }
                    email.setBody(body);
                    mailMessage.setText(email.getBody());

                    javaMailSender.send(mailMessage);

                    System.out.println("Mail sent!");

                } catch (Exception e) {

                    System.out.println("Error while sending mail!");
                    throw new BadRequestException("Error while sending mail!");

                }

            }

        }



        return meetingDto;

    }

    public List<MeetingDto> getAll(String header) {

        List<Meeting> meetings = meetingRepository.findAllByOrderByDateDescStartDesc();
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public MeetingDto getById(String id, String header) {

        Optional<Meeting> meetingOptional = meetingRepository.findById(id);
        if(meetingOptional.isEmpty()) {
            throw new ResourceNotFoundException("Meeting with id " + id + " not found!");
        }
        Meeting meeting = meetingOptional.get();
        MeetingDto meetingDto = meetingToMeetingDto(meeting, header);

        return meetingDto;

    }

    public List<MeetingDto> getByDate(String dateString, String header) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, dateFormatter);

        List<Meeting> meetings = meetingRepository.findByDateOrderByDateAscStartAsc(date);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public List<MeetingDto> getByEmployee(Long employeeId, String header) {

        List<Meeting> meetings = meetingRepository.findByParticipantsEmployeeIdOrderByDateDescStartDesc(employeeId);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public List<MeetingDto> getByDateAndEmployee(String dateString, Long employeeId, String header) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, dateFormatter);

        List<Meeting> meetings = meetingRepository.findByDateAndParticipantsEmployeeId(date, employeeId);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public List<MeetingDto> getByDateAndResponseAndEmployee(String dateString, Response response, Long employeeId, String header) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, dateFormatter);

        List<Meeting> meetings = meetingRepository.findByDateAndParticipantsResponseAndParticipantsEmployeeId(date, response, employeeId);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }


    public List<MeetingDto> getFutureMeetings(String header) {

        LocalDate date = LocalDate.now();
        List<Meeting> meetings = meetingRepository.findByDateAfterOrderByDateAscStartAsc(date);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public List<MeetingDto> getPastMeetings(String header) {

        LocalDate date = LocalDate.now();
        List<Meeting> meetings = meetingRepository.findByDateBeforeOrderByDateDescStartDesc(date);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public List<MeetingDto> getFutureAndRecentPastMeetings(String header) {

        LocalDate dateStart = LocalDate.now().minusMonths(1);
        LocalDate dateEnd = LocalDate.now().plusMonths(1);
        List<Meeting> meetings = meetingRepository.findByDateBetweenOrderByDateDescStartDesc(dateStart, dateEnd);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public List<MeetingDto> getFutureMeetingsByEmployee (Long employeeId, String header) {

        LocalDate date = LocalDate.now();
        List<Meeting> meetings = meetingRepository.findByDateAfterAndParticipantsEmployeeIdOrderByDateDescStartAsc(date, employeeId);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public List<MeetingDto> getPastMeetingsByEmployee (Long employeeId, String header) {

        LocalDate date = LocalDate.now();
        List<Meeting> meetings = meetingRepository.findByDateBeforeAndParticipantsEmployeeIdOrderByDateDescStartDesc(date, employeeId);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    public List<MeetingDto> getFutureAndRecentPastMeetingsByEmployee (Long employeeId, String header) {

        LocalDate dateStart = LocalDate.now().minusMonths(1);
        LocalDate dateEnd = LocalDate.now().plusMonths(1);
        List<Meeting> meetings = meetingRepository.findByDateBetweenAndParticipantsEmployeeIdOrderByDateDescStartDesc(dateStart, dateEnd, employeeId);
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting m : meetings) {
            MeetingDto meetingDto = meetingToMeetingDto(m, header);
            meetingDtos.add(meetingDto);
        }

        return meetingDtos;

    }

    @Transactional
    public MeetingDto changeDate(String id, String dateString, String startString, String endString, String header) {

        Optional<Meeting> meetingOptional = meetingRepository.findById(id);
        if(meetingOptional.isEmpty()) {
            throw new ResourceNotFoundException("Meeting with id " + id + " not found!");
        }
        Meeting meeting = meetingOptional.get();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate date = LocalDate.parse(dateString, dateFormatter);
        LocalTime start = LocalTime.parse(startString, timeFormatter);
        LocalTime end = LocalTime.parse(endString, timeFormatter);

        if(date.isEqual(meeting.getDate()) && start.equals(meeting.getStart()) && end.equals(meeting.getEnd())) {
            throw new BadRequestException("Meeting date is already the same!");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new BadRequestException("Meeting date " + date + " has been exceeded!");
        }

        if (date.isAfter(LocalDate.now().plusMonths(1))) {
            throw new BadRequestException("Meeting date " + date + " is more than 1 month away!");
        }

        if ((date.getDayOfWeek() == DayOfWeek.SATURDAY) || (date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
            throw new BadRequestException("Meeting date " + date + " is on the weekend!");
        }

        if (start.isBefore(LocalTime.parse("08:00", timeFormatter))) {
            throw new BadRequestException("Meeting start time " + start + " must be after 08:00!");
        }

        if (end.isAfter(LocalTime.parse("20:00", timeFormatter))) {
            throw new BadRequestException("Meeting end time " + end + " must be before 20:00!");
        }

        if (date.isEqual(LocalDate.now()) && start.isBefore(LocalTime.now())) {
            throw new BadRequestException("Meeting start time " + start + " has been exceeded!");
        }

        if (end.isBefore(start.plusMinutes(15))) {
            throw new BadRequestException("Meeting must be at least 15 minutes long!");
        }

        if (end.isAfter(start.plusHours(2))) {
            throw new BadRequestException("Meeting must be at last 2 hours long!");
        }

        List<Participant> participants = meeting.getParticipants();
        if(!participants.isEmpty()) {
            for (Participant p : participants) {

                Employee e2 = getEmployeeById(p.getEmployeeId(), header);
                if(!e2.isEnabled()) {
                    throw new BadRequestException("Employee " + e2.getFirstName() + " " + e2.getLastName() + " is disabled!");
                }

                List<Meeting> meetings = meetingRepository.findByDateAndParticipantsEmployeeId(date, p.getEmployeeId());
                if (!meetings.isEmpty()) {
                    for (Meeting m : meetings) {
                        if(!id.equals(m.getId())) {
                            if (date.isEqual(m.getDate())) {
                                Response response = null;
                                for (Participant p2 : m.getParticipants()) {
                                    if (p2.getEmployeeId() == p.getEmployeeId()) {
                                        response = p2.getResponse();
                                    }
                                }
                                if (response == YES) {
                                    if (start.isBefore(m.getStart()) && end.isAfter(m.getStart())) {
                                        Employee e = getEmployeeById(p.getEmployeeId(), header);
                                        throw new BadRequestException("Employee " + e.getFirstName() + " " + e.getLastName() + " has another meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                                    } else if ((start.isAfter(m.getStart()) || start.equals(m.getStart())) && start.isBefore(m.getEnd())) {
                                        Employee e = getEmployeeById(p.getEmployeeId(), header);
                                        throw new BadRequestException("Employee " + e.getFirstName() + " " + e.getLastName() + " has another meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                                    }
                                }
                            }
                        }
                    }
                }

                List<BusyTime> busyTimes = busyTimeRepository.findByDateAndEmployeeId(date, p.getEmployeeId());
                if (!busyTimes.isEmpty()) {
                    for (BusyTime bt : busyTimes) {
                        if (date.isEqual(bt.getDate())) {
                            if (start.isBefore(bt.getStart()) && end.isAfter(bt.getStart())) {
                                Employee e = getEmployeeById(p.getEmployeeId(), header);
                                throw new BadRequestException("Employee " + e.getFirstName() + " " + e.getLastName() + " is busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                            } else if ((start.isAfter(bt.getStart()) || start.equals(bt.getStart())) && start.isBefore(bt.getEnd())) {
                                Employee e = getEmployeeById(p.getEmployeeId(), header);
                                throw new BadRequestException("Employee " + e.getFirstName() + " " + e.getLastName() + " is busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                            }
                        }
                    }
                }

            }
        }

        meeting.setDate(date);
        meeting.setStart(start);
        meeting.setEnd(end);

        LocalDateTime updatedAt = LocalDateTime.now();
        meeting.setUpdatedAt(updatedAt);

        meetingRepository.save(meeting);

        MeetingDto meetingDto = meetingToMeetingDto(meeting, header);

        return meetingDto;

    }

    @Transactional
    public MeetingDto addParticipant(String id, Long employeeId, String header) {

        Optional<Meeting> meetingOptional = meetingRepository.findById(id);
        if(meetingOptional.isEmpty()) {
            throw new ResourceNotFoundException("Meeting with id " + id + " not found!");
        }
        Meeting meeting = meetingOptional.get();

        List<Participant> participants = new ArrayList<>();
        for (Participant p : meeting.getParticipants()) {
            if (employeeId == p.getEmployeeId()) {
                throw new BadRequestException("Employee with id " + employeeId + " already added!");
            }
            participants.add(p);
        }

        Employee employee = getEmployeeById(employeeId, header);

        if(!employee.isEnabled()) {
            throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " is disabled!");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate date = meeting.getDate();
        LocalTime start = meeting.getStart();
        LocalTime end = meeting.getEnd();

        List<Meeting> meetings = meetingRepository.findByDateAndParticipantsEmployeeId(meeting.getDate(), employeeId);
        if (!meetings.isEmpty()) {
            for (Meeting m : meetings) {
                if (meeting.getDate().isEqual(m.getDate())) {
                    Response response = null;
                    for (Participant p : m.getParticipants()) {
                        if (p.getEmployeeId() == employeeId) {
                            response = p.getResponse();
                        }
                    }
                    if (response == YES) {
                        if (start.isBefore(m.getStart()) && end.isAfter(m.getStart())) {
                            throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " has another meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                        } else if ((start.isAfter(m.getStart()) || start.equals(m.getStart())) && start.isBefore(m.getEnd())) {
                            throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " has another meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                        }
                    }
                }
            }
        }

        List<BusyTime> busyTimes = busyTimeRepository.findByDateAndEmployeeId(meeting.getDate(), employeeId);
        if (!busyTimes.isEmpty()) {
            for (BusyTime bt : busyTimes) {
                if (date.isEqual(bt.getDate())) {
                    if (start.isBefore(bt.getStart()) && end.isAfter(bt.getStart())) {
                        throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " is busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                    } else if ((start.isAfter(bt.getStart()) || start.equals(bt.getStart())) && start.isBefore(bt.getEnd())) {
                        throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " is busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                    }
                }
            }
        }

        Participant participant = new Participant();
        participant.setResponse(NO);
        participant.setEmployeeId(employeeId);
        participants.add(participant);
        meeting.setParticipants(participants);

        LocalDateTime updatedAt = LocalDateTime.now();
        meeting.setUpdatedAt(updatedAt);

        meetingRepository.save(meeting);

        MeetingDto meetingDto = meetingToMeetingDto(meeting, header);

        return meetingDto;

    }

    @Transactional
    public MeetingDto removeParticipant(String id, Long employeeId, String header) {

        Optional<Meeting> meetingOptional = meetingRepository.findById(id);
        if(meetingOptional.isEmpty()) {
            throw new ResourceNotFoundException("Meeting with id " + id + " not found!");
        }
        Meeting meeting = meetingOptional.get();

        boolean employeeFound = false;
        List<Participant> participants = new ArrayList<>();
        for (Participant p : meeting.getParticipants()) {
            if (employeeId != p.getEmployeeId()) {
                participants.add(p);
            } else {
                employeeFound = true;
            }
        }
        meeting.setParticipants(participants);

        if (!employeeFound) {
            throw new BadRequestException("Employee with id " + employeeId + " not found!");
        }

        LocalDateTime updatedAt = LocalDateTime.now();
        meeting.setUpdatedAt(updatedAt);

        meetingRepository.save(meeting);

        MeetingDto meetingDto = meetingToMeetingDto(meeting, header);

        return meetingDto;

    }

    @Transactional
    public MeetingDto addResponse(String id, Long employeeId, Response response, String header) {

        Optional<Meeting> meetingOptional = meetingRepository.findById(id);
        if(meetingOptional.isEmpty()) {
            throw new ResourceNotFoundException("Meeting with id " + id + " not found!");
        }
        Meeting meeting = meetingOptional.get();

        Employee employee = getEmployeeById(employeeId, header);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate date = meeting.getDate();
        LocalTime start = meeting.getStart();
        LocalTime end = meeting.getEnd();

        if(response.equals(YES)) {
            List<Meeting> meetings = meetingRepository.findByDateAndParticipantsEmployeeId(meeting.getDate(), employeeId);
            if (!meetings.isEmpty()) {
                for (Meeting m : meetings) {
                    if(!id.equals(m.getId())) {
                        if (meeting.getDate().isEqual(m.getDate())) {
                            Response response2 = null;
                            for (Participant p : m.getParticipants()) {
                                if (p.getEmployeeId() == employeeId) {
                                    response2 = p.getResponse();
                                }
                            }
                            if (response2 == YES) {
                                if (start.isBefore(m.getStart()) && end.isAfter(m.getStart())) {
                                    throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " has another meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                                } else if ((start.isAfter(m.getStart()) || start.equals(m.getStart())) && start.isBefore(m.getEnd())) {
                                    throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " has another meeting from " + m.getStart().format(timeFormatter) + " to " + m.getEnd().format(timeFormatter) + "!");
                                }
                            }
                        }
                    }
                }
            }

            List<BusyTime> busyTimes = busyTimeRepository.findByDateAndEmployeeId(meeting.getDate(), employeeId);
            if (!busyTimes.isEmpty()) {
                for (BusyTime bt : busyTimes) {
                    if (date.isEqual(bt.getDate())) {
                        if (start.isBefore(bt.getStart()) && end.isAfter(bt.getStart())) {
                            throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " is busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                        } else if ((start.isAfter(bt.getStart()) || start.equals(bt.getStart())) && start.isBefore(bt.getEnd())) {
                            throw new BadRequestException("Employee " + employee.getFirstName() + " " + employee.getLastName() + " is busy from " + bt.getStart().format(timeFormatter) + " to " + bt.getEnd().format(timeFormatter) + "!");
                        }
                    }
                }
            }
        }


        List<Participant> participants = meeting.getParticipants();
        for (Participant p : participants) {
            if (employeeId == p.getEmployeeId()) {
                p.setResponse(response);
            }
        }
        meeting.setParticipants(participants);

        meetingRepository.save(meeting);

        MeetingDto meetingDto = meetingToMeetingDto(meeting, header);

        return meetingDto;

    }

    @Transactional
    public String delete(String id, String header) {

        Optional<Meeting> meetingOptional = meetingRepository.findById(id);
        if(meetingOptional.isEmpty()) {
            throw new ResourceNotFoundException("Meeting with id " + id + " not found!");
        }
        Meeting meeting = meetingOptional.get();

        meetingRepository.deleteById(id);

        return "Meeting with id " + id + " deleted!";

    }

}
