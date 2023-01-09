package com.beobia.user.service;

import com.beobia.user.entity.User;
import com.beobia.user.security.tokens.ConfirmationToken;
import com.beobia.user.security.tokens.ResetToken;
import com.beobia.user.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class RegistrationService {
    private BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    private static final int CONFIRMATION_TOKEN_MINUTES    =   10;
    private static final int RESET_TOKEN_MINUTES    =   10;
    private static final String CONFIRMATION_EMAIL_URL     =   "/api/v1/register/confirm?token=";
    private static final String RESET_EMAIL_URL     =   "/api/v1/register/recovery/confirm?token=";

    @Value("${APPLICATION_PATH}")
    private String APPLICATION_PATH;


    public ResponseEntity<String> registerNewUser(User user) throws Exception {
        if(user != null && !user.getUsername().isEmpty() && user.getPassword().length() > 0) {
            try {
                String encodedPass = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPass);
                userService.saveUser(user);;

                String token = generateConfirmationToken(user);
                String emailLink = APPLICATION_PATH + CONFIRMATION_EMAIL_URL + token;
                emailService.send(
                        user.getEmail(),
                        emailService.buildEmail(user.getFirstName(), emailLink, "confirm your email")
                        );


                return ResponseEntity.ok(token);

            } catch (Exception e) {

                System.out.println(e.getCause());
                throw new Exception(e.getMessage(), e.getCause());
            }
        }
        return ResponseEntity.badRequest().body("Error with registration request");
    }


    public String generateConfirmationToken(User user) {

        org.springframework.security.core.userdetails.User newUser =
                new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());

        String token =
                jwtUtil.generateAccessToken(newUser, CONFIRMATION_TOKEN_MINUTES);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusSeconds( CONFIRMATION_TOKEN_MINUTES * 60 * 1000),
                null,
                user
        );
        tokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = tokenService
                .getConfirmationToken(token)
                .orElseThrow(()->
                        new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token has expired");
        }

        tokenService.setConfirmedAt(token);
        userService.assignRoleToUser(
                        confirmationToken.getUser().getUsername(),
                "ROLE_USER");
        return "confirmed";
    }

    public ResponseEntity<String> resetPasswordTokenSender(String email) throws Exception {

        if(email != null && !email.isEmpty()) {
            try {
                User user = userService.getUserByEmail(email).orElseThrow(()->new IllegalStateException("Email Not Found"));
                if(user !=null) {

                    String token = generateResetToken(user);
                    String emailString = emailService.buildResetEmail(APPLICATION_PATH + RESET_EMAIL_URL + token);
                    emailService.sendResetEmail(email, emailString);

                    return ResponseEntity.ok(token);
                }
            } catch (Exception e) {

                System.out.println(e.getCause());
                throw new Exception(e.getMessage(), e.getCause());
            }
        }
        return ResponseEntity.badRequest().body("Error with registration request");
    }

    public ResponseEntity<String> resetPassword(String password, String token) {
        boolean confirmToken = confirmResetToken(token);
        if(confirmToken){
            User user = tokenService.getUserByResetToken(token);
            userService.updatePassword(user, password);
        }

        return ResponseEntity.ok("Password Changed");
    }

    public String generateResetToken(User user) {

        org.springframework.security.core.userdetails.User newUser =
                new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());

        String token =
                jwtUtil.generateAccessToken(newUser, RESET_TOKEN_MINUTES);

        ResetToken resetToken = new ResetToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusSeconds( CONFIRMATION_TOKEN_MINUTES * 60 * 1000),
                null,
                user
        );
        tokenService.saveResetToken(resetToken);
        return token;
    }


    @Transactional
    public boolean confirmResetToken(String token){
        ResetToken resetToken = tokenService
                .getResetToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if(resetToken.getUsedAt() != null){
            throw new IllegalStateException("password already reset!");
        }

        LocalDateTime expiredAt = resetToken.getExpiredAt();

        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token has expired");
        }

        tokenService.setUsedAt(token);
        return true;
    }

    public RegistrationService(BCryptPasswordEncoder passwordEncoder, TokenService tokenService, UserService userService, JwtUtil jwtUtil, EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

}
