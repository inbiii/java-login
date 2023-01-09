package com.beobia.user.controller;

import com.beobia.user.entity.User;
import com.beobia.user.request.PassResetRequest;
import com.beobia.user.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;


    @PostMapping("register")
    private ResponseEntity<String> register(@RequestBody User user) throws Exception {
        try {
            return registrationService.registerNewUser(user);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Error with registration");
        }
    }


    @GetMapping("register/confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }


    @PostMapping("register/recover")
    public ResponseEntity<String> recoveryRequest(@RequestBody PassResetRequest passResetRequest) throws Exception {
        return registrationService.resetPasswordTokenSender(passResetRequest.getEmail());
    }


    @PostMapping("register/recover/confirm")
    public ResponseEntity<String> passwordReset(@RequestBody String password, @RequestParam("token") String token) throws Exception {
        return registrationService.resetPassword(password, token);

    }

}
