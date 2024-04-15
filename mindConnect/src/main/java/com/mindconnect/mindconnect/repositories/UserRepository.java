package com.mindconnect.mindconnect.repositories;

import com.mindconnect.mindconnect.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAddress(String emailAddress);

    User findById(UUID id);

    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<User> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String query, String query1);


    @Query("SELECT u from users u WHERE " +
            "u.emailAddress != :currentUserEmail AND (" +
            "LOWER(u.firstName) LIKE CONCAT('%', LOWER(:query), '%')" +
            "OR LOWER(u.lastName) LIKE CONCAT('%', LOWER(:query), '%'))")
    List<User> searchUsers(String query, String currentUserEmail);
}
