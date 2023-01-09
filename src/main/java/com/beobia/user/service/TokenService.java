package com.beobia.user.service;

import com.beobia.user.entity.User;
import com.beobia.user.repository.ConfirmationTokenRepository;
import com.beobia.user.repository.ResetTokenRepository;
import com.beobia.user.security.tokens.ConfirmationToken;
import com.beobia.user.security.tokens.ResetToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class TokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ResetTokenRepository resetTokenRepository;


    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getConfirmationToken(String token){
       return confirmationTokenRepository.findByToken(token);
    }

    public Optional<ResetToken> getResetToken(String token){
        return resetTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken =
                confirmationTokenRepository
                        .findByToken(token)
                        .orElseThrow(()-> new IllegalStateException("token not found"));
        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }

    public void setUsedAt(String token){
        ResetToken resetToken =
                resetTokenRepository
                        .findByToken(token)
                        .orElseThrow(()-> new IllegalStateException("token not found"));
        resetToken.setUsedAt(LocalDateTime.now());
    }

    public User getUserByResetToken(String token){
        return  resetTokenRepository
                .findUserByToken(token)
                .orElseThrow(()-> new IllegalStateException("token not found"));
    }
    public void saveResetToken(ResetToken token) { resetTokenRepository.save(token); }
}
