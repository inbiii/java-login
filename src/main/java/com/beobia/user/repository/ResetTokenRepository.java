package com.beobia.user.repository;

import com.beobia.user.entity.User;
import com.beobia.user.security.tokens.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    Optional<ResetToken> findByToken(String token);
    Optional<User> findUserByToken(String token);
}
