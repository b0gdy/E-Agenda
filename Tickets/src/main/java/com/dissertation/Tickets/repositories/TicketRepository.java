package com.dissertation.Tickets.repositories;

import com.dissertation.Tickets.entities.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findAllByOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc();

    List<Ticket> findByEnabledOrderByStatusDescPriorityAscCreatedAtAsc(boolean enabled);

    List<Ticket> findByTitleLikeOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(String title);

    List<Ticket> findByTitleLikeAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(String title, boolean enabled);

    List<Ticket> findByTitleLikeAndClientIdOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(String title, Long clientId);

    List<Ticket> findByTitleLikeAndClientIdAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(String title, Long clientId, boolean enabled);

    List<Ticket> findByClientIdOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(Long clientId);

    List<Ticket> findByClientIdAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(Long clientId, boolean enabled);

    List<Ticket> findByEmployeeIdOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(Long employeeId);

    List<Ticket> findByEmployeeIdAndEnabledOrderByEnabledDescStatusDescPriorityAscCreatedAtAsc(Long employeeId, boolean enabled);


    // find by company id ordered

    // find by company id and enabled ordered

    // find by title and company id ordered

    // find by title and company id and enabled ordered

}
