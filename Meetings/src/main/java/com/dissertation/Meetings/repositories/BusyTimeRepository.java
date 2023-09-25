package com.dissertation.Meetings.repositories;

import com.dissertation.Meetings.entities.BusyTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusyTimeRepository extends MongoRepository<BusyTime, String> {

    List<BusyTime> findByDateAndEmployeeId(LocalDate date, Long employeeId);

    List<BusyTime> findAllByOrderByDateDescStartDesc();

    List<BusyTime> findByDateOrderByDateAscStartAsc(LocalDate date);

    List<BusyTime> findByEmployeeIdOrderByDateDescStartDesc(Long employeeId);

    List<BusyTime> findByDateAndEmployeeIdOrderByDateAscStartAsc(LocalDate date, Long employeeId);

    List<BusyTime> findByDateAfterOrderByDateAscStartAsc(LocalDate date);

    List<BusyTime> findByDateBeforeOrderByDateDescStartDesc(LocalDate date);

    List<BusyTime> findByDateBetweenOrderByDateDescStartDesc(LocalDate dateStart, LocalDate dateEnd);

    List<BusyTime> findByDateAfterAndEmployeeIdOrderByDateDescStartAsc(LocalDate date, Long employeeId);

    List<BusyTime> findByDateBeforeAndEmployeeIdOrderByDateDescStartDesc(LocalDate date, Long employeeId);

    List<BusyTime> findByDateBetweenAndEmployeeIdOrderByDateDescStartDesc(LocalDate dateStart, LocalDate dateEnd, Long employeeId);

}
