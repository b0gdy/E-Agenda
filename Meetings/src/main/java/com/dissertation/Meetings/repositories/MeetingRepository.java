package com.dissertation.Meetings.repositories;

import com.dissertation.Meetings.entities.Meeting;
import com.dissertation.Meetings.entities.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {

    List<Meeting> findByDateAndParticipantsEmployeeId(LocalDate date, Long employeeId);

    List<Meeting> findByDateAndParticipantsResponseAndParticipantsEmployeeId(LocalDate date, Response response, Long employeeId);

    List<Meeting> findAllByOrderByDateDescStartDesc();

    List<Meeting> findByDateOrderByDateAscStartAsc(LocalDate date);

    List<Meeting> findByParticipantsEmployeeIdOrderByDateDescStartDesc(Long employeeId);

    List<Meeting> findByDateAfterOrderByDateAscStartAsc(LocalDate date);

    List<Meeting> findByDateBeforeOrderByDateDescStartDesc(LocalDate date);

    List<Meeting> findByDateBetweenOrderByDateDescStartDesc(LocalDate dateStart, LocalDate dateEnd);

    List<Meeting> findByDateAfterAndParticipantsEmployeeIdOrderByDateDescStartAsc(LocalDate date, Long employeeId);

    List<Meeting> findByDateBeforeAndParticipantsEmployeeIdOrderByDateDescStartDesc(LocalDate date, Long employeeId);

    List<Meeting> findByDateBetweenAndParticipantsEmployeeIdOrderByDateDescStartDesc(LocalDate dateStart, LocalDate dateEnd, Long employeeId);

}
