package com.dissertation.authentication.repositories;

import com.dissertation.authentication.entities.Employee;
import com.dissertation.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("SELECT u FROM User u WHERE u.userName = :userName")

    Optional<User> findByUserName(String userName);

    List<User> findAllByOrderByEnabledDesc();

    List<User> findAllByEnabled(Boolean enabled);

//    @Modifying
//    @Query(value = "DELETE dissertation_authentication.user_roles WHERE user_id = :id", nativeQuery = true)
//    void deleteUserRolesByUserId(Long id);

}
