package com.sch.crane.cranewebbackend_v2.Data.Repository.User;


import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.userEmail =:userEmail")
    Optional<User> findByUserEmail(@Param("userEmail") String userEmail);

    @Query("select u from User u where u.userEmail =:userEmail")
    Optional<User> findWithAuthoritiesByUserEmail(@Param("userEmail") String Email);

    User findOneWithAuthoritiesByUserName(String username);
}
