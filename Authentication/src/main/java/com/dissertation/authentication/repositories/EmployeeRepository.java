package com.dissertation.authentication.repositories;

import com.dissertation.authentication.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.userName = :userName")
    Optional<Employee> findByUserName(String userName);

    List<Employee> findAllByOrderByEnabledDesc();

    List<Employee> findAllByEnabled(Boolean enabled);

}



