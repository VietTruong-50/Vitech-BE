package com.hust.vitech.Repository;

import com.hust.vitech.Model.Role;
import com.hust.vitech.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    @Query("select u from User u join u.roles r where r.name = :role")
    Page<User> findAllByRole(String role, Pageable pageable);
}
