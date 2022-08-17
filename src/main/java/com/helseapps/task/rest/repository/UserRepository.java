package com.helseapps.task.rest.repository;

import com.helseapps.task.rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /*@Query(value = "SELECT u FROM User u WHERE CONCAT(u.username, ' ', u.firstName, ' ', u.lastName, ' ', u.address, ' ', " +
            "u.birthDay, ' ', u.email, ' ', u.gender, ' ', u.phoneNumber, ' ', u.note, ' ') LIKE %:keyword%")
    List<User> search(@Param("keyword") String keyword);*/

    @Query(value = "SELECT u FROM User u WHERE CONCAT(COALESCE(u.username, ' '), COALESCE(u.firstName, ' '), " +
            "COALESCE(u.lastName, ' '),COALESCE( u.address, ' '), COALESCE(u.email, ' '), COALESCE(u.gender, ' ')" +
            ", COALESCE(u.birthDay, ' '), COALESCE(u.phoneNumber, ' '), COALESCE(u.note, ' ')) LIKE %?1%")
    List<User> search(@Param("keyword") String keyword);

    @Query(value = "SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

}
