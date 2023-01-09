package com.beobia.user.service;

import com.beobia.user.entity.Role;
import com.beobia.user.entity.User;
import com.beobia.user.repository.RoleRepository;
import com.beobia.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public org.springframework.security.core.userdetails.User loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new IllegalStateException("User not Found"));

        log.info("User found in database {}", username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
                                authorities.add(new SimpleGrantedAuthority(role.getName()));
                            });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(userRepository.findAll());
    }


    public Optional<User> getUser(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) throws Exception {
        return userRepository.findByEmail(email);
    }

    public void assignRoleToUser(String username, String roleName){
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new IllegalStateException("User not Found"));
        if(user != null) {
            Role role = roleRepository.findByName(roleName);
            if(role != null) {
                user.getRoles().add(role);
            }
        }
    }

    public Role saveRole(Role role){
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void updatePassword(User user, String password) {
        User currentUser = userRepository
                                .findByUsername(user.getUsername())
                                .orElseThrow(() -> new IllegalStateException("user not found for password update"));
        currentUser.setPassword(password);
    }
}
