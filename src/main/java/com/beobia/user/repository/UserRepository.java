package com.beobia.user.repository;

import com.beobia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username) throws UsernameNotFoundException;

    Optional<User> findByEmail(String email) throws Exception;
}
