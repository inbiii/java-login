package com.beobia.user.services;

import com.beobia.user.repository.UserRepository;
import com.beobia.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    private String FAKE_NAME        = "John";
    private String FAKE_PASSWORD    = "password1";

//    @Test
//    public void loadByUsernameTest_SuccessCondition(){
//        Mockito.when(repository.findByUsername(FAKE_NAME))
//                .thenReturn(new User(FAKE_NAME, ));
//        service.loadUserByUsername()
//    }
}
