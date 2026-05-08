package com.example.demo.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface usersRepository extends JpaRepository<users, Long> {
    boolean existsByEmail(String email);

    Optional<users> findByEmail(String email);
}
